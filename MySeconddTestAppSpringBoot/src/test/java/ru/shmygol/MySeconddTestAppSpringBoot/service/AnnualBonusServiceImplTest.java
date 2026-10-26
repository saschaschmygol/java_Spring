package ru.shmygol.MySeconddTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Positions;

import java.time.Year;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {

        // given
        Positions position = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salary = 100000.00;

        // when
        double result = new AnnualBonusServiceImpl().calculate(position, salary, bonus, workDays);

        // then
        double expected = Year.now().isLeap() ? 361481.48148148146 : 360493.8271604938;
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @EnumSource(value = Positions.class, names = {"TL", "PO", "TPM", "CTO"})
    void calculateQuarterlyBonusForManager(Positions position) {
        AnnualBonusService service = new AnnualBonusServiceImpl();

        double result = service.calculateQuarterlyBonus(position, 100000, 2, Year.now().length());

        assertThat(result).isEqualTo(50000 * position.getPositionCoefficient());
    }

    @ParameterizedTest
    @EnumSource(value = Positions.class, names = {"DEV", "HR"})
    void calculateQuarterlyBonusForNonManager(Positions position) {
        AnnualBonusService service = new AnnualBonusServiceImpl();

        assertThatThrownBy(() -> service.calculateQuarterlyBonus(position, 100000, 2, 243))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Квартальная премия доступна только руководителям");
    }
}
