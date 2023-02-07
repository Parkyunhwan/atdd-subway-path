package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class LineTest {
    @Test
    void addSection() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-900");
        int distance = 10;

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section section = new Section(신분당선, 강남역, 양재역, distance);

        // when
        신분당선.addSection(section);

        // then
        assertThat(신분당선.getSections()).containsExactlyElementsOf(List.of(section));
    }

    @Test
    void getStations() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-900");
        int distance = 10;

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section section = new Section(신분당선, 강남역, 양재역, distance);
        신분당선.getSections().add(section);

        // when
        List<Station> 신분당선_지하철 = 신분당선.getStations();

        // then
        assertThat(신분당선_지하철).containsExactlyElementsOf(List.of(강남역, 양재역));
    }

    @Test
    @DisplayName("Section을 하나만 가지고 있는 Line 삭제 테스트")
    void removeSectionLineHasOneSection() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-900");
        int distance = 10;

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Section section = new Section(신분당선, 강남역, 양재역, distance);
        신분당선.getSections().add(section);

        // when
        신분당선.removeSection(양재역);

        // then
        List<Station> 신분당선_지하철 = 신분당선.getStations();
        assertThat(신분당선_지하철).containsExactlyElementsOf(List.of());
    }

    @Test
    @DisplayName("Section을 하나이상 가지고 있는 Line 삭제 테스트")
    void removeSection() {
        // given
        Line 신분당선 = new Line("신분당선", "bg-red-900");
        int distance = 10;

        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Section 강남_양재_구간 = new Section(신분당선, 강남역, 양재역, distance);
        Section 양재_양재시민의숲_구간 = new Section(신분당선, 양재역, 양재시민의숲역, distance);
        신분당선.getSections().add(강남_양재_구간);
        신분당선.getSections().add(양재_양재시민의숲_구간);

        // when
        신분당선.removeSection(양재시민의숲역);

        // then
        List<Station> 신분당선_지하철 = 신분당선.getStations();
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(신분당선.getSections()).containsExactlyElementsOf(List.of(강남_양재_구간));
            softAssertions.assertThat(신분당선_지하철).containsExactlyElementsOf(List.of(강남역, 양재역));
        });
    }
}
