package ua.nure.salenko.model;

import org.apache.log4j.Logger;

public class Filter {

    private static final Logger LOGGER = Logger.getLogger(Filter.class);

    private String model;
    private String dealer;
    // fridge, stove, washer
    private String type;
    // no frost,drop frost, manual || electric, gas || vertical, front
    private String specialParameter2;
    private Long worthMin;
    private Long worthMax;
    private Long lengthMin;
    private Long lengthMax;
    private Long widthMin;
    private Long widthMax;
    private Long heightMin;
    private Long heightMax;
    // volume, count of burners or weight load
    private Long specialParameter1Min;
    private Long specialParameter1Max;

    public Filter() {
        LOGGER.info("Filter#constructor");
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecialParameter2() {
        return specialParameter2;
    }

    public void setSpecialParameter2(String specialParameter2) {
        this.specialParameter2 = specialParameter2;
    }

    public Long getWorthMin() {
        return worthMin;
    }

    public void setWorthMin(Long worthMin) {
        this.worthMin = worthMin;
    }

    public Long getWorthMax() {
        return worthMax;
    }

    public void setWorthMax(Long worthMax) {
        this.worthMax = worthMax;
    }

    public Long getLengthMin() {
        return lengthMin;
    }

    public void setLengthMin(Long lengthMin) {
        this.lengthMin = lengthMin;
    }

    public Long getLengthMax() {
        return lengthMax;
    }

    public void setLengthMax(Long lengthMax) {
        this.lengthMax = lengthMax;
    }

    public Long getWidthMin() {
        return widthMin;
    }

    public void setWidthMin(Long widthMin) {
        this.widthMin = widthMin;
    }

    public Long getWidthMax() {
        return widthMax;
    }

    public void setWidthMax(Long widthMax) {
        this.widthMax = widthMax;
    }

    public Long getHeightMin() {
        return heightMin;
    }

    public void setHeightMin(Long heightMin) {
        this.heightMin = heightMin;
    }

    public Long getHeightMax() {
        return heightMax;
    }

    public void setHeightMax(Long heightMax) {
        this.heightMax = heightMax;
    }

    public Long getSpecialParameter1Min() {
        return specialParameter1Min;
    }

    public void setSpecialParameter1Min(Long specialParameter1Min) {
        this.specialParameter1Min = specialParameter1Min;
    }

    public Long getSpecialParameter1Max() {
        return specialParameter1Max;
    }

    public void setSpecialParameter1Max(Long specialParameter1Max) {
        this.specialParameter1Max = specialParameter1Max;
    }

    @Override
    public String toString() {
        return "Filter [model=" + model + ", dealer=" + dealer + ", type=" + type + ", specialParameter2="
                + specialParameter2 + ", worthMin=" + worthMin + ", worthMax=" + worthMax + ", lengthMin=" + lengthMin
                + ", lengthMax=" + lengthMax + ", widthMin=" + widthMin + ", widthMax=" + widthMax + ", heightMin="
                + heightMin + ", heightMax=" + heightMax + ", specialParameter1Min=" + specialParameter1Min
                + ", specialParameter1Max=" + specialParameter1Max + "]";
    }

}
