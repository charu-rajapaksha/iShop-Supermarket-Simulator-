package iShop;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

public class Customer extends Observable implements Runnable{
	
	Image image;
	private double x = 0;
	private double y = 0;
	private int directionX = 0;
	private int directionY = 0;
	CustomerType customerType;
	Indicate indicate = Indicate.none;
	boolean indicating = false;
	private ShoppingPathLbl destination;
	private ShoppingPathLbl origin;
	private Status status;
	static int idIncrement = 1;
	private int id;
	private int angle;
	private SimulatorModel simModel = SimulatorModel.simModel();
	OutputPanel outputPanel;
	private int testCase;
	boolean kill = false;
	
	public enum CustomerType {female, male, family, hurry}
	public enum ShoppingPathLbl {a, b, c, d, e, f, g}
	public enum Status{none, aIn, aOut, bIn, bOut, cIn, cOut, dIn, dOut, eIn, eOut, fIn, fOut, rb, waitingRb, cpA, cpB, cpC, cpD}
	public enum Indicate{none, left, right}

	public Customer(int testCase) {
		this.testCase = testCase;
		
		construct();
		
		if (testCase == 1) {
			origin = ShoppingPathLbl.c;
			destination = ShoppingPathLbl.c;

			x = SimulatorModel.pathCIn.getEdgeX();
			y = SimulatorModel.pathCIn.getEdgeY();
			angle = SimulatorModel.pathCIn.getAngle();
		}
				
		setIndicatorDirection();
		notifyNewCustomer();
		
	}
	
	//test case 2
	public Customer(ShoppingPathLbl destination) {
		

		construct();
		
		origin = ShoppingPathLbl.c;

		x = SimulatorModel.pathCIn.getEdgeX();
		y = SimulatorModel.pathCIn.getEdgeY();
		angle = SimulatorModel.pathCIn.getAngle();
		
		this.destination = destination;
		
		setIndicatorDirection();
		notifyNewCustomer();
	}
	
	private void construct() {
		status = Status.none;
		
		id = idIncrement;
		idIncrement++;
		
		setObserver(SimulatorView.simView());
		
		setObserver(OutputPanel.outputPanel());
		
		randomiseAttributes();
	}
	
		
	private void randomiseAttributes(){
		Random random = new Random();
		
		//generate random customerType attribute
		int colourInt = random.nextInt(4) + 1;
		switch(colourInt){
		case 1:
			customerType = CustomerType.family;
			try {
				image = ImageIO.read(new File("src/iShop/images/family_Customer.png"));
			} catch (IOException e) {
				// TODO Auto-	generated catch block	
				e.printStackTrace();
			}
			break;
		case 2:
			customerType = CustomerType.male;
			try {
				image = ImageIO.read(new File("src/iShop/images/male_Customer.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			customerType = CustomerType.female;
			try {
				image = ImageIO.read(new File("src/iShop/images/female_Customer.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 4:
			customerType = CustomerType.hurry;
			try {
				image = ImageIO.read(new File("src/iShop/images/hurry_Customer.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		
		//fetches paths that the Customer can use
		ArrayList<ShoppingPathLbl> startingRoads = simModel.getStartingRoads();
		origin = startingRoads.get(random.nextInt(startingRoads.size()));
		
		ArrayList<ShoppingPathLbl> endingRoads = simModel.getEndingRoads();
		destination = endingRoads.get(random.nextInt(endingRoads.size()));
						
		
		Road path = null;
		//sets status and required location based on origin
		switch(origin){
		case a:
			status = Status.cIn;
			path = SimulatorModel.pathCIn;
			
			break;
		case b:
			status = Status.cIn;
			path = SimulatorModel.pathCIn;
			
			break;
		case c:
			status = Status.cIn;
			path = SimulatorModel.pathCIn;
			
			break;
		case d:
			status = Status.cIn;
			path = SimulatorModel.pathCIn;
			
			break;
		}
		
		x = path.getEdgeX();
		y = path.getEdgeY();
		angle = path.getAngle();
	}
	
	//Main Customer method
	@Override
	public void run() {
		
		try {
			performRoadIn();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		while (!checkRoundaboutClear()){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (testCase == 1) {
			return;
		}
		
		joinRoundabout();
		
		performRoundabout();
		
		exitRoundabout();
		
		exitRoad();
		
		simModel.removeCustomer(this);
		status = Status.none;
		
		notifyCustomerKilled();
	}
	
	
	private void performRoadIn() throws InterruptedException {
		int distanceFromTarget = 0;
		int roadEnd;
		
		switch (origin) {
		case a:
			roadEnd = SimulatorModel.pathCIn.getrbX();

			status = Status.cIn;
			directionX = -5;
			distanceFromTarget = (int) (x - roadEnd);
			break;
			
		case b:
			roadEnd = SimulatorModel.pathCIn.getrbX();

			status = Status.cIn;
			directionX = -5;
			distanceFromTarget = (int) (x - roadEnd);
			break;
			
		case c:
			roadEnd = SimulatorModel.pathCIn.getrbX();
			
			status = Status.cIn;
			directionX = -5;
			distanceFromTarget = (int) (x - roadEnd);
			break;
			
		case d:
			roadEnd = SimulatorModel.pathCIn.getrbX();

			status = Status.cIn;
			directionX = -5;
			distanceFromTarget = (int) (x - roadEnd);
			break;
		}
		
		//while the Customer hasn't arrived at the middle keep checking if clear then move further
		while (distanceFromTarget > 0) {
			if (roadIsClear()) {
				moveOnRoad();
				distanceFromTarget -= 5;
				
				if (distanceFromTarget < 80) {
					indicating = true;
				}
			}
			
		}
		
		status = Status.waitingRb;
	}
	
	private void moveOnRoad() throws InterruptedException{
		
		x += directionX;
		y += directionY;
		
		notifyCustomerToView();
		
		Thread.sleep(50);
	}
	
	//check if road is clear to move on and return true or false
	private boolean roadIsClear() {
		CopyOnWriteArrayList<Customer> Customers = new CopyOnWriteArrayList<Customer>(simModel.getCustomers());
		
		for (Customer Customer : Customers){
			if (Customer != null && (Customer.status == this.status || Customer.status == Status.waitingRb)){
				int CustomerX = Customer.getX();
				int CustomerY = Customer.getY();

				int distanceRequired = 75;
				if (directionX > 0){
					if (CustomerX > x && (CustomerX - x) < distanceRequired){
						return false;
					}
					
				} else if (directionX < 0){
					if (CustomerX < x && (x - CustomerX) < distanceRequired){
						return false;
					}
					
				} else if (directionY > 0){
					if (CustomerY > y && (CustomerY - y) < distanceRequired){
						return false;
					}
					
				} else if (directionY < 0){
					if (CustomerY < y && (y - CustomerY) < distanceRequired){
						return false;
					}
				}				
			}
		}		
		return true;
	}
	
	//method for going around the Meat Corner
	private void performRoundabout() {
		int[][] rbCtrlPs = simModel.getRBControlPoints();
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		Status lastCP = Status.none;	
		
		switch (destination) {
		case a:
			lastCP = Status.cpD;
			break;
		case b:
			lastCP = Status.cpA;
			break;
		case c:
			lastCP = Status.cpB;
			break;
		case d:
			lastCP = Status.cpC;
			break;
		}
		
		//while not at the last exist of the Meat Corner
		while (status != lastCP) {
			
			switch (status) {
			case cpA:
				lerpX = rbCtrlPs[1][0];
				lerpY = rbCtrlPs[1][1];
				lerpAngle = rbCtrlPs[1][2];
				status = Status.cpB;
				break;
				
			case cpB:
				lerpX = rbCtrlPs[2][0];
				lerpY = rbCtrlPs[2][1];
				lerpAngle = rbCtrlPs[2][2];
				status = Status.cpC;
				break;
			
			case cpC:
				lerpX = rbCtrlPs[3][0];
				lerpY = rbCtrlPs[3][1];
				lerpAngle = rbCtrlPs[3][2];
				status = Status.cpD;
				break;
			
			case cpD:
				lerpX = rbCtrlPs[0][0];
				lerpY = rbCtrlPs[0][1];
				lerpAngle = rbCtrlPs[0][2];
				status = Status.cpA;
				break;
			}
			
			//move to next control point
			lerp(lerpX, lerpY, lerpAngle);
		}
	}
	
	private boolean checkRoundaboutClear(){
		CopyOnWriteArrayList<Customer> Customers = new CopyOnWriteArrayList<Customer>(simModel.getCustomers());
		
		//check if there's space to join
		switch (origin) {
		case a:
			for (Customer Customer : Customers){
				if (Customer.indicate != Indicate.left && (Customer.status == Status.cpB || Customer.status == Status.cpA || Customer.status == Status.cpD)){
					return false;
				}
			}
			break;
		case b:
			for (Customer Customer : Customers){
				if (Customer.indicate != Indicate.left && (Customer.status == Status.cpC || Customer.status == Status.cpB || Customer.status == Status.cpA)){
					return false;
				}
			}
			break;
		case c:
			for (Customer Customer : Customers){
				if (Customer.indicate != Indicate.left && (Customer.status == Status.cpD || Customer.status == Status.cpC || Customer.status == Status.cpB)){
					return false;
				}
			}
			break;
		case d:
			for (Customer Customer : Customers){
				if (Customer.indicate != Indicate.left && (Customer.status == Status.cpA || Customer.status == Status.cpD || Customer.status == Status.cpC)){
					return false;
				}
			}
			break;
		}		
		return true;
	}
	
	private void setIndicatorDirection() {
		switch (origin) {
		case a:
			if (destination == ShoppingPathLbl.d) {
				indicate = Indicate.left;
			} else if (destination == ShoppingPathLbl.b || destination == ShoppingPathLbl.c) {
				indicate = Indicate.right;
			}
			break;
			
		case b:
			if (destination == ShoppingPathLbl.d) {
				indicate = Indicate.left;
			} else if (destination == ShoppingPathLbl.b || destination == ShoppingPathLbl.c) {
				indicate = Indicate.right;
			}
			break;
			
		case c:
			if (destination == ShoppingPathLbl.d) {
				indicate = Indicate.left;
			} else if (destination == ShoppingPathLbl.b || destination == ShoppingPathLbl.c) {
				indicate = Indicate.right;
			}
			break;

		case d:
			if (destination == ShoppingPathLbl.d) {
				indicate = Indicate.left;
			} else if (destination == ShoppingPathLbl.b || destination == ShoppingPathLbl.c) {
				indicate = Indicate.right;
			}
			break;
		}
	}
	
	private void joinRoundabout() {
		int[][] rbCtrlPs = simModel.getRBControlPoints();
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		
		switch (origin) {
		case a:
			lerpX = rbCtrlPs[2][0];
			lerpY = rbCtrlPs[2][1];
			lerpAngle = rbCtrlPs[2][2];
			status = Status.cpC;
			break;
		case b:
			lerpX = rbCtrlPs[2][0];
			lerpY = rbCtrlPs[2][1];
			lerpAngle = rbCtrlPs[2][2];
			status = Status.cpC;
			break;
		case c: 
			lerpX = rbCtrlPs[2][0];
			lerpY = rbCtrlPs[2][1];
			lerpAngle = rbCtrlPs[2][2];
			status = Status.cpC;
			
			break;
		case d:
			lerpX = rbCtrlPs[2][0];
			lerpY = rbCtrlPs[2][1];
			lerpAngle = rbCtrlPs[2][2];
			status = Status.cpC;
			break;
		}
		
		notifyCustomerEnteredRoundabout();
		
		lerp(lerpX, lerpY, lerpAngle);
		
	}
	
	private void exitRoundabout() {
		indicate = Indicate.left;
		
		int lerpX = 0;
		int lerpY = 0;
		int lerpAngle = 0;
		Status lerpStatus = Status.none;
		
		switch (destination) {
		case a:
			lerpX = SimulatorModel.pathCOut.getrbX();
			lerpY = SimulatorModel.pathCOut.getrbY();
			lerpAngle = SimulatorModel.pathCOut.getAngle();
			lerpStatus = Status.cOut;
			break;
			
		case b:
			lerpX = SimulatorModel.pathCOut.getrbX();
			lerpY = SimulatorModel.pathCOut.getrbY();
			lerpAngle = SimulatorModel.pathCOut.getAngle();
			lerpStatus = Status.cOut;
			break;
			
		case c:
			lerpX = SimulatorModel.pathCOut.getrbX();
			lerpY = SimulatorModel.pathCOut.getrbY();
			lerpAngle = SimulatorModel.pathCOut.getAngle();
			lerpStatus = Status.cOut;
			break;
			
		case d:
			lerpX = SimulatorModel.pathCOut.getrbX();
			lerpY = SimulatorModel.pathCOut.getrbY();
			lerpAngle = SimulatorModel.pathCOut.getAngle();
			lerpStatus = Status.cOut;
			break;
		}
		
		lerp(lerpX, lerpY, lerpAngle);
		
		//set status to final road
		status = lerpStatus;
		indicating = false;
		
		notifyCustomerLeftRoundabout();
	}
	
	
	private void lerp(int lerpX, int lerpY, int lerpAngle) {
		int moveAngle = 0;
		int slowness = 12;
		
		double moveX = (lerpX - x)/slowness;
		double moveY = (lerpY - y)/slowness;
		
		moveAngle = lerpAngle-angle;
		
		//determine the direction to spin based on current and desired angle
		if (!(lerpAngle-angle < 180 && lerpAngle - angle > -180)){
			if (moveAngle > 180){
				moveAngle = moveAngle-360;
			}
			else if (moveAngle < -180){
				moveAngle = moveAngle+360;
			}
		}
		
		moveAngle /= slowness;
		
		//for every fraction of the movement, move a little
		for (int i = 0; i < slowness; i++) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x += moveX;
			y += moveY;
			angle += moveAngle;
			
			//update view on position
			notifyCustomerToView();
		}
		
		//coordinates set to exact goal as they may be off a little
		x = lerpX;
		y = lerpY;
		angle = lerpAngle;
	}
	
	private void exitRoad(){
		
		int edgeX = 0;
		int edgeY = 0;
		directionY = 0;
		directionX = 0;
		
		switch (destination) {
		case a:
			edgeX = SimulatorModel.pathCOut.getEdgeX();
			edgeY = SimulatorModel.pathCOut.getEdgeY();
			directionX = 5;
			status = Status.cOut;
			break;
			
		case b:
			edgeX = SimulatorModel.pathCOut.getEdgeX();
			edgeY = SimulatorModel.pathCOut.getEdgeY();
			directionX = 5;
			status = Status.cOut;
			break;
			
		case c:
			edgeX = SimulatorModel.pathCOut.getEdgeX();
			edgeY = SimulatorModel.pathCOut.getEdgeY();
			directionX = 10;
			status = Status.cOut;
			break;
			
		case d:
			edgeX = SimulatorModel.pathCOut.getEdgeX();
			edgeY = SimulatorModel.pathCOut.getEdgeY();
			directionX = 5;
			status = Status.cOut;
			break;
		}
				
		//while the customer has not reached the edge of the screen
		while(x!=edgeX || y!=edgeY){
			try {
				moveOnRoad();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//if x or y are our of the sight of the view exit method
			if (x < -30 || x > 1075 || y < -60 || y > 1000){
				return;
			}
		}
	}
		

	public int getID() {return id;}
	public int getX() {return (int) x;}
	public int getY() {return (int) y;}
	public double getAngle() {return angle;}
	public Status getStatus() {return status;}
	public ShoppingPathLbl getOrigin() {return origin;}
	public ShoppingPathLbl getDestination() {return destination;}
	public CustomerType getColour() {return customerType;}
	public boolean getIndicating() {return indicating;}	
	public Indicate getIndicator() {return indicate;}
	
	public void setX(int x) {this.x=x;}
	public void setY(int y) {this.y=y;}
	
	public void kill() {kill = true;}
	static public void resetCustomerIDs() {idIncrement = 1;}
	
	
	
	//observer methods
	private void notifyCustomerKilled() {
		String text;
		
		text = "Killed customer #" + id +
				" - CustomerType: " + customerType.toString() +
				", Origin: " + origin.toString() + 
				", Destination: " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCustomerEnteredRoundabout() {
		String text;
		
		text = "Customer #" + id +
				" has ENTERED the Meat Corner from road " + origin.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCustomerLeftRoundabout() {
		String text;
		
		text = "Customer #" + id +
				" has LEFT the Meat Corner " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
	private void notifyCustomerToView() {
		if (!kill) {
			setChanged();
			notifyObservers(this);
		}
	}
	
	private void notifyNewCustomer() {
		String text;
		
		text = "New Customer #" + id +
				" - CustomerType: " + customerType.toString() +
				", Origin: " + origin.toString() + 
				", Destination: " + destination.toString(); 
				
		setChanged();
		notifyObservers(text);
	}
	
		
	private void setObserver(Observer observer) {
		this.addObserver(observer);
	}
}
