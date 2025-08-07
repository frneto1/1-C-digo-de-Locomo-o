package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.Joystick;
 

public class Robot extends TimedRobot {
    private final VictorSPX m_leftDrive = new VictorSPX(3);
    private final VictorSPX m_rightDrive = new VictorSPX(2);
    private final VictorSPX m_leftDrive2 = new VictorSPX(4);
    private final VictorSPX m_rightDrive2 = new VictorSPX(1);

    double velocidadeD;
    double velocidadeE;

    Joystick bob = new Joystick(0);

    double Ltrigger;
    double Rtrigger;

    double magnitude;
    double magnitude2;
    double sen;
    double sen2;


    double x_left;
    double x_right;
    double y_left;
    double y_right;

    double anaE;
    double anaD;

    double m_speed;
    int POV;

    boolean a;
    boolean b;
    boolean x;

  public Robot() {

    m_rightDrive.setInverted(true);
    m_rightDrive2.setInverted(true);
    m_leftDrive.setInverted(false);
    m_leftDrive2.setInverted(false);

    m_rightDrive.configNeutralDeadband(0.04);
    m_rightDrive2.configNeutralDeadband(0.04);
    m_leftDrive.configNeutralDeadband(0.04);
    m_leftDrive2.configNeutralDeadband(0.04);

    m_rightDrive.setNeutralMode(NeutralMode.Brake);
    m_rightDrive2.setNeutralMode(NeutralMode.Brake);
    m_leftDrive.setNeutralMode(NeutralMode.Brake);
    m_leftDrive2.setNeutralMode(NeutralMode.Brake);
  }

  public void teleopPeriodic() {
    button();
    dash();
  
    Ltrigger = bob.getRawAxis(2);
    Rtrigger = bob.getRawAxis(3);
  
    x_left = bob.getRawAxis(0);
    y_left = bob.getRawAxis(1);
    x_right = bob.getRawAxis(4);
    y_right = bob.getRawAxis(5);
  
    POV = bob.getPOV();
    a = bob.getRawButton(1);
    b = bob.getRawButton(2);
    x = bob.getRawButton(3);
  
    if (POV != -1) {
      pov();
    } else if (Rtrigger > 0.04) {
      Rtrigger();
    } else if (Ltrigger > 0.04) {
      Ltrigger();
    } else {
      if (Math.abs(x_left) < 0.04 && Math.abs(y_left) < 0.04) {
        anaD();
      } else {
        anaE();
      }
    }
  
    m_rightDrive.set(ControlMode.PercentOutput, velocidadeD);
    m_rightDrive2.set(ControlMode.PercentOutput, velocidadeD);
    m_leftDrive.set(ControlMode.PercentOutput, velocidadeE);
    m_leftDrive2.set(ControlMode.PercentOutput, velocidadeE);
  }
  

  public void dash(){
    SmartDashboard.putNumber("RTrigger", Rtrigger);
    SmartDashboard.putNumber("LTrigger", Ltrigger);
    SmartDashboard.putNumber("m_speed", m_speed);
    SmartDashboard.putNumber("velocidadeD", velocidadeD);
    SmartDashboard.putNumber("VelocidadeE", velocidadeE);
    SmartDashboard.putBoolean("a", a);
    SmartDashboard.putBoolean("b", b);
    SmartDashboard.putBoolean("x", x);
    SmartDashboard.putNumber("Analogico Esquerdo", anaE);
  }

  public void button(){
    if (a){
      m_speed = 0.25;
    } else if (b){
      m_speed = 0.5;
    } else if (x){
      m_speed = 1;
    }
  }
 
  public void pov() {
    switch(POV){
      case 0:
        velocidadeE = m_speed;
        velocidadeD = m_speed;
        break;
      case 90: 
        velocidadeE = m_speed;
        velocidadeD = -m_speed;
        break;
      case 180:
        velocidadeE = -m_speed;
        velocidadeD = -m_speed;
        break;
      case 270:
        velocidadeE = -m_speed;
        velocidadeD = m_speed;
        break;
      case -1:
        velocidadeE = 0;
        velocidadeD = 0;
      }
  }
 
  public void Ltrigger(){
    if (Ltrigger > 0.04){
      velocidadeE = Ltrigger * -m_speed;
      velocidadeD = Ltrigger * -m_speed;
    } else{
      velocidadeE = 0;
      velocidadeD = 0;
    }
  }

  public void Rtrigger(){
    if (Rtrigger > 0.04){
      velocidadeD = Rtrigger * m_speed;
      velocidadeE = Rtrigger * m_speed;
    } else{
      velocidadeD = 0;
      velocidadeE = 0;
    }
  }

  public void anaE() {

    double magnitude = Math.hypot(y_left,x_left);

    magnitude = Math.min(1, Math.max(-1, magnitude));

    double sen = y_left/magnitude;


    
    if (y_left < 0 && x_left > 0){
      velocidadeE = m_speed * magnitude;
      velocidadeD = (2 * sen + 1) * magnitude * m_speed * -1;
    } 
    else if (y_left < 0 && x_left < 0){
      velocidadeE = (m_speed * (2 * sen + 1)) * magnitude * -1;
      velocidadeD = m_speed * magnitude;
    }
    else if (y_left > 0 && x_left < 0){
      velocidadeE = -(m_speed * (2 * sen - 1)) * magnitude;
      velocidadeD = -m_speed * magnitude;
    }
    else if (y_left > 0 & x_left > 0){
      velocidadeE = -m_speed * magnitude;
      velocidadeD = -(m_speed * (2 * sen - 1)) * magnitude;
    }
  }
  public void anaD() {

    double magnitude2 = Math.sqrt(y_right * y_right + x_right * x_right);
    double sen2 = y_right/magnitude2;

    Math.max(1, magnitude2);
    Math.max(1, magnitude2);

    
    if (y_right < 0 && x_right > 0){
      velocidadeE = m_speed * magnitude2;
      velocidadeD = (m_speed * sen2) * magnitude2 * -1;
    } 
    else if (y_right < 0 && x_right < 0){
      velocidadeE = (m_speed * sen2) * magnitude2 * -1;
      velocidadeD = m_speed * magnitude2;
    }
    else if (y_right > 0 && x_right < 0){
      velocidadeE = -(m_speed * sen2) * magnitude2;
      velocidadeD = -m_speed * magnitude2;
    }
    else if (y_right > 0 & x_right > 0){
      velocidadeE = -m_speed * magnitude2;
      velocidadeD = -(m_speed * sen2) * magnitude2;
    }

  }
  }
