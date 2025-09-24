package dto;

public class AmountDTO {
    private String from;
    private String to;
    private float amount;
    private float convertedAmount;

    public AmountDTO(String from, String to, float amount, float convertedAmount){
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
    }

    public AmountDTO(float convertedAmount) {
        this.convertedAmount = convertedAmount;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(float convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}
