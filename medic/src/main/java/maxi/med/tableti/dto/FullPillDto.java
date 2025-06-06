package maxi.med.tableti.dto;

import java.util.List;

public record FullPillDto(String title, List<String> features, String description, String imageUrl) {}
