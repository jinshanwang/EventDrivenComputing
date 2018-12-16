import java.text.DecimalFormat;

public class EnhancedReactionController implements Controller {
	private Gui display;
	private Random ran;
	private int counter;									//counter for tick()

	private boolean IsInsertCoin;         //the Insert coin state
	private boolean IsPressGo;						//the game start state
	private boolean IsWait;								//the waitingtime state
	private boolean IsTimer;							//the Timer state
	private boolean IsDisplayMeasuredTime;//stop and display time state
	private boolean IsAverage;						//show the Average time state
	private int waitingtime;							//declare a waitingtime variable
	private int gamecounter;							//declare a counter to record the times of game
	private double[] total_Time;					//declare a double type array to store the time of each of the game

	private static DecimalFormat df2 = new DecimalFormat("0.00");//the format of the display time

	@Override
	public void connect(Gui gui, Random rng) {
		this.display = gui;
		this.ran = rng;

	}

// initialization of the game
// setting all the state with false except the IsInsertCoin state
	@Override
	public void init() {

		this.IsInsertCoin = true;
		this.IsPressGo = false;
		this.IsWait = false;
		this.IsTimer = false;
		this.IsDisplayMeasuredTime = false;
		this.IsAverage = false;
		this.waitingtime = 0;
		this.gamecounter = 0;
		this.total_Time = new double[3];
		this.display.setDisplay("Insert coin");

		this.counter = 0;
	}

// when player insert a coin we need to set the IsPressGo state to true
// and close other state
	@Override
	public void coinInserted() {
		this.display.setDisplay("Press GO!");

		this.IsInsertCoin = false;
		this.IsPressGo = true;
		this.IsWait = false;
		this.IsTimer = false;
		this.IsDisplayMeasuredTime = false;
		this.IsAverage = false;
	}

// there are 4 states to be considered when the player press the Go/Stop button
	@Override
	public void goStopPressed() {
		// 1st situation
		// when it's IsPressGo state
		if (this.IsPressGo) {
			this.waitingtime = ran.getRandom(100, 250);
			this.display.setDisplay("Wait...");

			this.IsInsertCoin = false;
			this.IsPressGo = false;
			this.IsWait = true;
			this.IsTimer = false;
			this.IsDisplayMeasuredTime = false;
			this.IsAverage = false;
			this.counter = 0;
		}
		 // 2nd situation
		 // when it is wait state(illegal operation)
		else if (this.IsWait) {
			this.init();
		}
		// 3th situation
		// when it is IsTimer state
		else if (this.IsTimer) {

			this.IsInsertCoin = false;
			this.IsPressGo = false;
			this.IsWait = false;
			this.IsTimer = false;
			this.IsDisplayMeasuredTime = true;
			this.IsAverage = false;
			if (this.gamecounter < 3) {
				this.total_Time[this.gamecounter] = this.counter/1.00;
			}
			this.counter = 0;
		}
		 
		else if (this.IsDisplayMeasuredTime) {
			this.gamecounter++;
			if (this.gamecounter == 3) {
				double sum = 0.00;
				for (int i = 0; i < 3; i++) {
					sum = sum + this.total_Time[i];
				}

				this.display.setDisplay("Average=" + df2.format(sum / 300.00));
				this.IsInsertCoin = false;
				this.IsPressGo = false;
				this.IsWait = false;
				this.IsTimer = false;
				this.IsDisplayMeasuredTime = false;
				this.IsAverage = true;
			} else {
				this.waitingtime = ran.getRandom(100, 250);
				this.display.setDisplay("Wait...");

				this.IsInsertCoin = false;
				this.IsPressGo = false;

				this.IsWait = true;
				this.IsTimer = false;
				this.IsDisplayMeasuredTime = false;
				this.IsAverage = false;
				this.counter = 0;
			}
		} else if (this.IsAverage) {
			this.init();
		}
	}

	@Override
	public void tick() {
		if (this.IsPressGo) {
			this.counter++;
			if (this.counter == 1000) {
				this.init();
			}
		} else if (this.IsWait) {
			counter++;
			if (counter == this.waitingtime) {

				this.IsInsertCoin = false;
				this.IsPressGo = false;
				this.IsWait = false;
				this.IsTimer = true;
				this.IsDisplayMeasuredTime = false;
				this.IsAverage = false;

				this.counter = 0;
				this.display.setDisplay("0.00");
			}
		} else if (this.IsTimer == true) {
			this.counter++;
			this.display.setDisplay("" + df2.format(this.counter / 100.00));

			if (this.counter == 200) {
				this.init();
			}
		} else if (this.IsDisplayMeasuredTime) {
			this.counter++;
			if (this.counter == 300) {
				this.gamecounter++;
				if (this.gamecounter < 3) {
					this.waitingtime = ran.getRandom(100, 250);
					this.display.setDisplay("Wait...");
					this.IsInsertCoin = false;
					this.IsPressGo = false;
					this.IsWait = true;
					this.IsTimer = false;
					this.IsDisplayMeasuredTime = false;
					this.IsAverage = false;
					this.counter = 0;
				} else if (this.gamecounter == 3) {
					this.IsInsertCoin = false;
					this.IsPressGo = false;
					this.IsWait = false;
					this.IsTimer = false;
					this.IsDisplayMeasuredTime = false;
					this.IsAverage = true;
					this.counter = 0;
					double sum = 0.00;
					for (int i = 0; i < 3; i++) {
						sum = sum + this.total_Time[i];
					}
					this.display.setDisplay("Average= " + df2.format(sum / 300.00));
				}
			}
		} else if (this.IsAverage) {
			this.counter++;
			if (this.counter == 500) {
				this.init();
			}
		}

	}

}
