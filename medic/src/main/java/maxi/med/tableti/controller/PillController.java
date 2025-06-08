package maxi.med.tableti.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maxi.med.tableti.dto.FullPillDto;
import maxi.med.tableti.dto.PillDto;
import maxi.med.tableti.service.PillsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pills")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(
        origins = {
            "http://localhost:5173",
            "http://192.168.0.101:5173",
            "http://192.168.0.107:5173",
            "http://localhost:5173", "http://89.169.169.19:5173"
        })
public class PillController {

    private final PillsService pillsService;

    @GetMapping("/{page}")
    public List<PillDto> getPillsPage(@PathVariable int page) {
        log.info("чета пришло");
        return pillsService.getPillsPage(page);
    }

    @GetMapping("/pageCount")
    public Integer getPageCount() {
        log.info("123");
        return pillsService.getPageCount();
    }

    @GetMapping("/pill/{title}")
    public FullPillDto getPillInfo(@PathVariable String title) {
        log.info("prishlo");
        return pillsService.getPillInfo(title);
    }
}
