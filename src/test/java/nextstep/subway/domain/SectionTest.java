package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

class SectionTest {

    @DisplayName("구간 사이 거리가 충분한지 확인한다.")
    @Test
    void enoughDistance() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        boolean enoughDistance = section.isEnoughDistance(8);

        // then
        assertThat(enoughDistance).isTrue();
    }

    @DisplayName("새로 추가되는 구간이 기존 구간 사이에 추가할 수 있는 구간인지 확인한다. - 상행 지하철역 기준")
    @Test
    void betweenSectionAtUpStation() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.구로디지털단지역, Stub.신대방역, 4);
        boolean betweenSection = section.isBetweenSection(newSection);

        // then
        assertThat(betweenSection).isTrue();
    }

    @DisplayName("새로 추가되는 구간이 기존 구간 사이에 추가할 수 있는 구간인지 확인한다. - 하행 지하철역 기준")
    @Test
    void betweenSectionAtDownStation() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.신대방역, Stub.신림역, 6);
        boolean betweenSection = section.isBetweenSection(newSection);

        // then
        assertThat(betweenSection).isTrue();
    }

    @DisplayName("새로 추가되는 구간이 기존 구간의 상행 또는 하행으로 추가할 수 있는 구간인지 확인한다.")
    @Test
    void leafSection() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.대림역, Stub.구로디지털단지역, 8);
        boolean leafSection = section.isLeafSection(newSection);

        // then
        assertThat(leafSection).isTrue();
    }

    @DisplayName("기존 구간의 하행 지하철역을 수정한다.")
    @Test
    void changeDownSection() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.신대방역, Stub.신림역, 4);
        section.changeDownSection(newSection);

        // then
        assertThat(section.getUpStation()).isEqualTo(Stub.구로디지털단지역);
        assertThat(section.getDownStation()).isEqualTo(Stub.신대방역);
        assertThat(section.getDistance()).isEqualTo(6);
    }

    @DisplayName("기존 구간의 상행 지하철역을 수정한다.")
    @Test
    void changeUpSection() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.구로디지털단지역, Stub.신대방역, 3);
        section.changeUpSection(newSection);

        // then
        assertThat(section.getUpStation()).isEqualTo(Stub.신대방역);
        assertThat(section.getDownStation()).isEqualTo(Stub.신림역);
        assertThat(section.getDistance()).isEqualTo(7);
    }

    @DisplayName("기존 구간과 비교 구간의 상행과 하행 지하철역이 같은 역인지 확인한다.")
    @Test
    void matchStations() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.구로디지털단지역, Stub.신림역, 10);

        // then
        assertThat(section.matchStations(newSection)).isTrue();
    }

    @DisplayName("기존 구간과 비교 구간의 상행 지하철역이 같은 역인지 확인한다.")
    @Test
    void matchUpStation() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.구로디지털단지역, Stub.신대방역, 4);

        // then
        assertThat(section.matchUpStation(newSection.getUpStation())).isTrue();
    }

    @DisplayName("기존 구간과 비교 구간의 하행 지하철역이 같은 역인지 확인한다.")
    @Test
    void matchDownStation() {
        // given
        Section section = Stub.기본_구간_생성.get();

        // when
        Section newSection = new Section(Stub.대림역, Stub.신림역, 3);

        // then
        assertThat(section.matchDownStation(newSection.getDownStation())).isTrue();
    }

    @DisplayName("기존 지하철 구간에 새로운 지하철 구간과 중복되는 지하철역을 제거하고 합친다.")
    @Test
    void combineStation() {
        // given
        Section section = Stub.기본_구간_생성.get();
        Section newSection = new Section(Stub.구로디지털단지역, Stub.신대방역, 4);

        // when
        section.combine(newSection);

        // then
        assertThat(section.getDownStation()).isEqualTo(Stub.신대방역);
        assertThat(section.getDistance()).isEqualTo(14);
    }

    private static class Stub {
        public static final Station 대림역 = new Station("대림역");
        public static final Station 구로디지털단지역 = new Station("구로디지털단지역");
        public static final Station 신대방역 = new Station("신대방역");
        public static final Station 신림역 = new Station("신림역");
        public static final Supplier<Section> 기본_구간_생성 = () -> new Section(Stub.구로디지털단지역, Stub.신림역, 10);
    }
}