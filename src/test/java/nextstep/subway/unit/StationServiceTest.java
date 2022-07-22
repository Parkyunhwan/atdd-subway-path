package nextstep.subway.unit;

import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@SpringBootTest
@Transactional
public class StationServiceTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private StationRepository stationRepository;

    @Test
    void 역을_추가한다() {
        // when
        StationResponse response = stationService.saveStation(new StationRequest("암사역"));

        // then
        assertThat(response.getName()).isEqualTo("암사역");

    }

    @Test
    void 역을_조회한다() {
        // given
        Station station = stationRepository.save(new Station("암사역"));

        // when
        Station generatedStation = stationService.findById(station.getId());

        // then
        assertThat(generatedStation.getName()).isEqualTo("암사역");

    }

    @Test
    void 역_목록을_조회한다() {
        // given
        stationRepository.save(new Station("암사역"));
        stationRepository.save(new Station("모란역"));

        // when
        List<StationResponse> allStations = stationService.findAllStations();

        // then
        assertThat(allStations).hasSize(2);
    }

    @Test
    void 역을_삭제한다() {
        // given
        Station station = stationRepository.save(new Station("암사역"));

        // when
        stationService.deleteStationById(station.getId());

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> stationService.findById(station.getId()));
    }
}