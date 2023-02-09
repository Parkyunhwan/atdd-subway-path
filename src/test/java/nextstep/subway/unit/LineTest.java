package nextstep.subway.unit;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.exception.SectionAlreadyCreateStationException;
import nextstep.subway.exception.SectionUpStationNotMatchException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    private Line 신분당선;

    private Station 강남역;
    private Station 양재역;

    private Section 강남_양재_구간;

    private int distance = 10;

    @BeforeEach
    void setUp() {
        신분당선 = new Line("신분당선", "bg-red-900");
        distance = 10;

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        injectId(강남역, 1L);
        injectId(양재역, 2L);


        강남_양재_구간 = new Section(신분당선, 강남역, 양재역, distance);
        injectId(강남_양재_구간, 1L);
    }

    private static <T> void injectId(Object o, long id) {
        try {
            Field idField = o.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(o, id);
        } catch (Exception e) {
            throw new IllegalStateException("private필드 id값 inject중 예외발생", e);
        }
    }

    @Test
    @DisplayName("Line에 section이 비어있을 때 section추가 테스트")
    void addSectionForLineHasNoSection() {
        // given

        // when
        신분당선.addSection(강남_양재_구간);

        // then
        assertThat(신분당선.getSections()).containsExactlyElementsOf(List.of(강남_양재_구간));
    }

    @Test
    @DisplayName("Line에 section이 존재할 때 section추가 테스트")
    void addSectionForLineHasSection() {
        // given
        신분당선.addSection(강남_양재_구간);
        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Section 양재_양재시민의숲_구간 = new Section(신분당선, 양재역, 양재시민의숲역, distance);

        // when
        신분당선.addSection(양재_양재시민의숲_구간);

        // then
        assertThat(신분당선.getSections()).containsExactlyElementsOf(List.of(강남_양재_구간, 양재_양재시민의숲_구간));
    }

    @Test
    @DisplayName("Line의 상행역과 하행역이 모두 등록되어 있다면 예외 테스트")
    void failWhenLineDownStationDiffSectionUpStation() {
        // given
        신분당선.addSection(강남_양재_구간);

        Station 양재시민의숲역 = new Station("양재시민의숲역");
        Section 양재_양재시민의숲_구간 = new Section(신분당선, 양재역, 양재시민의숲역, distance);
        신분당선.addSection(양재_양재시민의숲_구간);

        // when, then
        Section 강남_양재시민의숲_구간 = new Section(신분당선, 강남역, 양재시민의숲역, distance);
        assertThatThrownBy(() -> 신분당선.addSection(강남_양재시민의숲_구간))
                .isInstanceOf(SectionAlreadyCreateStationException.class);
    }

    @Test
    @DisplayName("Line의 역들중에 Section의 하행역과 같은 것이 있다면 예외")
    void failWhenLineContainSectionDownStation() {
        // given
        신분당선.addSection(강남_양재_구간);

        Section 이미_등록된_강남역_포함구간 = new Section(신분당선, 양재역, 강남역, distance);
        injectId(이미_등록된_강남역_포함구간, 3L);

        // when, then
        assertThatThrownBy(() -> 신분당선.addSection(이미_등록된_강남역_포함구간))
                .isInstanceOf(SectionAlreadyCreateStationException.class);
    }

    @Test
    void getStations() {
        // given
        신분당선.getSections().add(강남_양재_구간);

        // when
        List<Station> 신분당선_지하철 = 신분당선.getStations();

        // then
        assertThat(신분당선_지하철).containsExactlyElementsOf(List.of(강남역, 양재역));
    }

    @Test
    @DisplayName("Section을 하나만 가지고 있는 Line 삭제 테스트")
    void removeSectionLineHasOneSection() {
        // given
        신분당선.getSections().add(강남_양재_구간);

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
        Station 양재시민의숲역 = new Station("양재시민의숲역");
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

    @Test
    @DisplayName("Line 정보 변경 테스트")
    void changeLineTest() {
        // given

        // when
        신분당선.changeNameAndColor("새로운노선명", "새로운컬러명");

        // then
        assertThat(신분당선.getName()).isEqualTo("새로운노선명");
        assertThat(신분당선.getColor()).isEqualTo("새로운컬러명");
    }

    @Test
    @DisplayName("노선 내 새 구간 위치 확인")
    void checkNewSectionPositionInLine() {
        // given
        신분당선.addSection(강남_양재_구간);


        Station 노원역 = new Station("노원역");
        // when
        boolean result = 신분당선.checkExistStation(노원역);

        // then
        assertThat(result).isFalse();
    }

    /*@Test
    @DisplayName("새 구간의 하행역이 기존구간의 중간에 삽입되는 경우")
    void insertStationMiddle() {
        // given
        신분당선.addSection(강남_양재_구간);

        Station 신논현역 = new Station("신논현역");
        Section 신논현_강남_구간 = new Section(신분당선, 신논현역, 강남역, distance);

        // when
        신분당선.addSection(신논현_강남_구간);

        // then
        assertThat(result).isFalse();
    }*/
}
