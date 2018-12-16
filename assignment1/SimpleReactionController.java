import java.text.DecimalFormat;

public class SimpleReactionController implements Controller {
	private Gui display;
	private Random ran;
	private int counter;

	private boolean IsInsertCoin;
	private boolean IsPressGo;
	private boolean IsWait;
	private boolean IsTimer;
	private boolean IsDisplayMeasuredTime;
	private int waitingtime;

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	@Override
	public void connect(Gui gui, Random rng) {
		this.display = gui;
		this.ran = rng;

	}

	@Override
	public void init() {

		this.IsInsertCoin = true;
		this.IsPressGo = false;
		this.IsWait = false;
		this.IsTimer = false;
		this.IsDisplayMeasuredTime = false;
		this.waitingtime = 0;
		this.display.setDisplay("Insert coin");

		this.counter = 0;
	}

	@Override
	public void coinInserted() {
		this.display.setDisplay("Press GO!");

		this.IsInsertCoin = false;
		this.IsPressGo = true;
		this.IsWait = false;
		this.IsTimer = false;
		this.IsDisplayMeasuredTime = false;
	}

	@Override
	public void goStopPressed() {

		if (this.IsPressGo) {
			this.waitingtime = ran.getRandom(100, 250);
			this.display.setDisplay("Wait...");

			this.IsInsertCoin = false;
			this.IsPressGo = false;
			this.IsWait = true;
			this.IsTimer = false;
			this.IsDisplayMeasuredTime = false;
		} else if (this.IsWait) {
			this.init();
		} else if (this.IsTimer) {

			this.IsInsertCoin = false;
			this.IsPressGo = false;
			this.IsWait = false;
			this.IsTimer = false;
			this.IsDisplayMeasuredTime = true;

		} else if (this.IsDisplayMeasuredTime) {
			this.init();
		}
	}

	@Override
	public void tick() {
		if (this.IsWait) {
			counter++;
			if (counter == this.waitingtime) {

				this.IsInsertCoin = false;
				this.IsPressGo = false;
				this.IsWait = false;
				this.IsTimer = true;
				this.IsDisplayMeasuredTime = false;

				counter = 0;
				this.display.setDisplay("0.00");
			}
		} else if (this.IsTimer == true) {
			this.counter++;

			this.display.setDisplay("" + df2.format(this.counter / 100.0));

			if (this.counter == 200) {
				this.init();
			}
		} else if (this.IsDisplayMeasuredTime) {
			this.counter++;
			if (this.counter == 300) {
				this.init();
			}
		}

	}

}
