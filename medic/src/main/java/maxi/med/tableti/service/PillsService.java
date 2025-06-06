package maxi.med.tableti.service;

import java.util.List;
import maxi.med.tableti.dto.FullPillDto;
import maxi.med.tableti.dto.PillDto;

public interface PillsService {
    List<PillDto> getPillsPage(int page);

    Integer getPageCount();

    List<PillDto> getUserPills(Long userId);

    PillDto addUserPill(Long userId, String pillTitle);

    FullPillDto getPillInfo(String title);
}
