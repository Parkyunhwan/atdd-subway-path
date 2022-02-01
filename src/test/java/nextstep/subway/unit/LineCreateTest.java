package nextstep.subway.unit;

import nextstep.subway.domain.Distance;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 추가에 대한 도메인 단위 테스트")
class LineCreateTest {

    Station 강남역;
    Station 판교역;
    Station 양재역;
    Line 신분당선;
    Distance 강남_판교_거리 = Distance.valueOf(7);
    Distance 판교_양재_거리 = Distance.valueOf(2);

    @BeforeEach
    void setLine() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
        양재역 = new Station("양재역");
        신분당선 = new Line("신분당선", "yellow", 강남역, 판교역, 강남_판교_거리);
    }

    @DisplayName("구간 목록 마지막에 새로운 구간을 추가할 경우")
    @Test
    void addSection() {
        // given
        Section 판교_양재 = new Section(신분당선, 판교역, 양재역, 판교_양재_거리);

        // when
        신분당선.addSection(판교_양재);

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 판교역, 양재역);
    }

    @DisplayName("상행역을 기준으로 역 사이에 새로운 역을 등록한다")
    @Test
    void addStationBetweenStationsBasedOnUpStation() {
        // given
        Section 강남_양재 = new Section(신분당선, 강남역, 양재역, Distance.valueOf(5));

        // when
        신분당선.addSection(강남_양재);

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 판교역);
        assertThat(신분당선.getSectionAt(0).getDistance()).isEqualTo(Distance.valueOf(5));
        assertThat(신분당선.getSectionAt(1).getDistance()).isEqualTo(Distance.valueOf(2));
    }

    @DisplayName("하행역을 기준으로 역 사이에 새로운 역을 등록한다")
    @Test
    void addStationBetweenStationsBasedOnDownStation() {
        // given
        Section 양재_판교 = new Section(신분당선, 양재역, 판교역, 판교_양재_거리);

        // when
        신분당선.addSection(양재_판교);

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 양재역, 판교역);
        assertThat(신분당선.getSectionAt(0).getDistance()).isEqualTo(Distance.valueOf(5));
        assertThat(신분당선.getSectionAt(1).getDistance()).isEqualTo(Distance.valueOf(2));
    }

    @DisplayName("새로운 역을 상행 종점으로 등록한다")
    @Test
    void addStationBaseOnLastUpStation() {
        // given
        Section 양재_강남 = new Section(신분당선, 양재역, 강남역, Distance.valueOf(2));

        // when
        신분당선.addSection(양재_강남);

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(양재역, 강남역, 판교역);
        assertThat(신분당선.getSectionAt(0).getDistance()).isEqualTo(Distance.valueOf(2));
        assertThat(신분당선.getSectionAt(1).getDistance()).isEqualTo(Distance.valueOf(7));
    }

    @DisplayName("새로운 역을 하행 종점으로 등록한다")
    @Test
    void addStationBaseOnLastDownStation() {
        // given
        Section 판교_양재 = new Section(신분당선, 판교역, 양재역, 판교_양재_거리);

        // when
        신분당선.addSection(판교_양재);

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(2);
        assertThat(신분당선.getStations()).containsExactly(강남역, 판교역, 양재역);
        assertThat(신분당선.getSectionAt(0).getDistance()).isEqualTo(Distance.valueOf(7));
        assertThat(신분당선.getSectionAt(1).getDistance()).isEqualTo(Distance.valueOf(2));
    }

    @DisplayName("노선 조회시 응답되는 역 목록")
    @Test
    void getStationsSortedByUpToDownStation() {
        // given
        Station 분당역 = new Station("분당역");
        Section 양재_강남 = new Section(신분당선, 양재역, 강남역, Distance.valueOf(2));
        Section 분당_양재 = new Section(신분당선, 분당역, 양재역, Distance.valueOf(3));
        신분당선.addSection(양재_강남);
        신분당선.addSection(분당_양재);

        // when
        List<Station> stations = 신분당선.getStations();

        // then
        assertThat(신분당선.getSectionSize()).isEqualTo(3);
        assertThat(stations).containsExactly(분당역, 양재역, 강남역, 판교역);
        assertThat(신분당선.getSectionAt(0).getDistance()).isEqualTo(Distance.valueOf(3));
        assertThat(신분당선.getSectionAt(1).getDistance()).isEqualTo(Distance.valueOf(2));
        assertThat(신분당선.getSectionAt(2).getDistance()).isEqualTo(Distance.valueOf(7));
    }
}