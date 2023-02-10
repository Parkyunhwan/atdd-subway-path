package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void addLast(Section Section) {
        sections.add(Section);
    }

    public List<Section> getSections() {
        if (sections.isEmpty()) {
            return sections;
        }
        List<Section> sectionResults = new ArrayList<>();
        Section currSection = getFirstSection();
        sectionResults.add(currSection);
        while (true) {
            Optional<Section> nextStationOpt = getNextSection(currSection);
            if (nextStationOpt.isEmpty()) {
                break;
            }
            currSection = nextStationOpt.get();
            sectionResults.add(currSection);
        }
        return sectionResults;
    }

    public Section getLastSection() {
        return getSections().get(getLastIndex());
    }

    public List<Station> getStations() {
        if (sections.isEmpty()) {
            return new ArrayList<>();
        }
        List<Station> stations = new ArrayList<>();

        Section currSection = getFirstSection();
        stations.add(currSection.getUpStation());
        while (true) {
            stations.add(currSection.getDownStation());
            Optional<Section> nextStationOpt = getNextSection(currSection);
            if (nextStationOpt.isEmpty()) {
                break;
            }
            currSection = nextStationOpt.get();
        }
        return stations;
    }

    private Optional<Section> getNextSection(Section currSection) {
        Station downStation = currSection.getDownStation();
        return sections.stream()
                .filter(section -> {
                    Station upStation = section.getUpStation();
                    return downStation.equals(upStation);
                })
                .findFirst();
    }

    public Section getFirstSection() {
        return sections.stream()
                .filter(section -> {
                    Station upStation = section.getUpStation();
                    return sections.stream()
                            .map(Section::getDownStation)
                            .noneMatch(upStation::equals);
                }).findFirst().get();
    }

    public void removeLastSection() {
        sections.remove(getLastIndex());
    }

    private int getLastIndex() {
        return sections.size() - 1;
    }

    public boolean checkExistStation(Station station) {
        return getStations().stream()
                .anyMatch(s -> s.equals(station));
    }

    public boolean containsStations(List<Station> stations) {
        List<Station> existStations = getStations();
        return new HashSet<>(existStations).containsAll(stations);
    }

    public void remove(Section existSection) {
        sections.remove(existSection);
    }
}
