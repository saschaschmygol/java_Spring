package ru.shmygol.MySeconddTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.shmygol.MySeconddTestAppSpringBoot.model.Positions;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {

    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int daysInYear = Year.now().length();
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }

    @Override
    public double calculateQuarterlyBonus(Positions positions, double salary, double bonus, int workDays) {
        if (!positions.isManager()) {
            throw new IllegalArgumentException("Квартальная премия доступна только руководителям");
        }
        return calculate(positions, salary, bonus, workDays) / 4;
    }

}
