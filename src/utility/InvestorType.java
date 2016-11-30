package utility;

public enum InvestorType {
	BMW("BMW"), TOYOTA("TOYOTA"), FIAT("FIAT");

	private String investorType;

	private InvestorType(String investorType) {
		this.investorType = investorType;
	}

	public String getCarType() {
		return investorType;
	}
}