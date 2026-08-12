package ffWork;

public abstract class Resource {
    private final String name;
    private final Money customHourlyRate;

    protected Resource(String name, Money customHourlyRate) {
        this.name = name;
        this.customHourlyRate = customHourlyRate;
    }

    public String getName() {
        return name;
    }
    public Money getCustomHourlyRate() {
        return customHourlyRate;
    }


    protected abstract Money baseRatePerHour();

    public abstract String describe();

    public Money hourlyRate() {
        if (customHourlyRate != null) {
            return customHourlyRate;
        } else {
            return baseRatePerHour();
        }
    }

    @Override
    public String toString() {
        return describe();
    }

}
