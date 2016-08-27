package ebondshark.jpa;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the trader database table.
 * 
 */
@Entity
@NamedQuery(name="Trader.findAll", query="SELECT t FROM Trader t")
public class Trader implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TRADER_TRADER_ID_GENERATOR", sequenceName="TRADER_ID")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TRADER_TRADER_ID_GENERATOR")
	private String trader_id;

	private String address;

	private String age;

	@Column(name="credit_rarting")
	private String creditRarting;

	private String password;

	@Column(name="phone_num")
	private String phoneNum;

	private String sex;

	@Column(name="trader_name")
	private String traderName;

	//bi-directional many-to-one association to Trade
	@OneToMany(mappedBy="trader")
	private List<Trade> trades;

	public Trader() {
	}

	public String getTrader_id() {
		return this.trader_id;
	}

	public void setTrader_id(String trader_id) {
		this.trader_id = trader_id;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAge() {
		return this.age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getCreditRarting() {
		return this.creditRarting;
	}

	public void setCreditRarting(String creditRarting) {
		this.creditRarting = creditRarting;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNum() {
		return this.phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSex() {
		return this.sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getTraderName() {
		return this.traderName;
	}

	public void setTraderName(String traderName) {
		this.traderName = traderName;
	}

	public List<Trade> getTrades() {
		return this.trades;
	}

	public void setTrades(List<Trade> trades) {
		this.trades = trades;
	}

	public Trade addTrade(Trade trade) {
		getTrades().add(trade);
		trade.setTrader(this);

		return trade;
	}

	public Trade removeTrade(Trade trade) {
		getTrades().remove(trade);
		trade.setTrader(null);

		return trade;
	}

}