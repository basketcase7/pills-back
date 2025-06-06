package maxi.med.tableti.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import maxi.med.tableti.dto.FullPillDto;
import maxi.med.tableti.dto.PillDto;
import maxi.med.tableti.entity.Feature;
import maxi.med.tableti.entity.Tablet;
import maxi.med.tableti.entity.TabletFeature;
import maxi.med.tableti.entity.User;
import maxi.med.tableti.entity.UserFeature;
import maxi.med.tableti.entity.UserTablet;
import maxi.med.tableti.exception.EntityNotFoundException;
import maxi.med.tableti.exception.IncompatibleException;
import maxi.med.tableti.mapper.PillMapper;
import maxi.med.tableti.repository.PillFeatureRepository;
import maxi.med.tableti.repository.PillsRepository;
import maxi.med.tableti.repository.UserFeatureRepository;
import maxi.med.tableti.repository.UserRepository;
import maxi.med.tableti.repository.UserTabletRepository;
import maxi.med.tableti.service.PillsService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PillsServiceImpl implements PillsService {

    private final PillsRepository pillsRepository;
    private final UserRepository userRepository;
    private final UserTabletRepository userTabletRepository;
    private final UserFeatureRepository userFeatureRepository;
    private final PillFeatureRepository pillFeatureRepository;

    @Override
    public List<PillDto> getPillsPage(int pageNumber) {
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 8);
        return pillsRepository.findAll(pageRequest).stream()
                .map(PillMapper::mapTabletToPillDto)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getPageCount() {
        long pillsCount = pillsRepository.count();
        if (pillsCount == 0) {
            return 0;
        } else {
            return (int) Math.ceil((double) pillsCount / 8);
        }
    }

    @Transactional
    @Override
    public List<PillDto> getUserPills(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userTabletRepository.findByUser(user).stream()
                .map(PillMapper::mapUserTabletToPillDto)
                .toList();
    }

    @Override
    @Transactional
    public PillDto addUserPill(Long userId, String pillTitle) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Tablet tablet =
                pillsRepository.findByTitle(pillTitle).orElseThrow(() -> new EntityNotFoundException("Pill not found"));

        UserTablet userTablet = userTabletRepository.findByUserAndTablet(user, tablet);
        if (userTablet != null) {
            throw new IllegalArgumentException("Вы уже добавили это средство");
        }

        List<String> userFeatures = new java.util.ArrayList<>(userFeatureRepository.findAllByUser(user).stream()
                .map(UserFeature::feature)
                .map(Feature::title)
                .toList());
        List<String> tabletFeatures = pillFeatureRepository.findAllByTablet(tablet).stream()
                .map(TabletFeature::feature)
                .map(Feature::title)
                .toList();

        userFeatures.retainAll(tabletFeatures);

        if (!userFeatures.isEmpty()) {
            throw new IncompatibleException("Вам противопоказано это средство");
        } else {
            UserTablet newUserTablet = new UserTablet();
            newUserTablet.user(user);
            newUserTablet.tablet(tablet);
            userTabletRepository.save(newUserTablet);
        }

        return PillMapper.mapTabletToPillDto(tablet);
    }

    @Transactional
    @Override
    public FullPillDto getPillInfo(String title) {
        Tablet tablet =
                pillsRepository.findByTitle(title).orElseThrow(() -> new EntityNotFoundException("Pill not found"));

        List<String> features = pillFeatureRepository.findAllByTablet(tablet).stream()
                .map(TabletFeature::feature)
                .map(Feature::title)
                .toList();

        return new FullPillDto(tablet.title(), features, tablet.description(), tablet.imageUrl());
    }
}
