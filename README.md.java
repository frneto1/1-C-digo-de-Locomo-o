package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PS4Controller.Button;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.util.sendable.Sendable;
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

 @Override
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

    if (POV != -1){
      pov();
    } else {
      if (Rtrigger == 0.04 && magnitude < 0.1 && magnitude2 < 0.01){
        Ltrigger();
      } else if (Ltrigger == 0.04 && magnitude < 0.01 && magnitude2 < 0.01){
        Rtrigger();
      }
    }

    a = bob.getRawButton(1);
    b = bob.getRawButton(2);
    x = bob.getRawButton(3);

    m_rightDrive.set(ControlMode.PercentOutput, velocidadeD);
    m_rightDrive2.set(ControlMode.PercentOutput, velocidadeD);
    m_leftDrive.set(ControlMode.PercentOutput, velocidadeE);
    m_leftDrive2.set(ControlMode.PercentOutput, velocidadeE);

    if (x_left < 0.04 && x_left > 0 && y_left > -0.04 && y_left < 0.04){
      anaD();
    } else {
      anaE();
    }
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
      velocidadeE = m_speed * 0;
      velocidadeD = m_speed * 0;
    }
  }

  public void Rtrigger(){
    if (Rtrigger > 0.04){
      velocidadeD = Rtrigger * m_speed;
      velocidadeE = Rtrigger * m_speed;
    } else{
      velocidadeD = m_speed * 0;
      velocidadeE = m_speed * 0;
    }
  }

  public void anaE() {

    double magnitude = Math.sqrt(y_left * y_left + x_left * x_left);
    double sen = y_left/magnitude;

    Math.max(1, velocidadeD);
    Math.max(1, velocidadeE);

    
    if (y_left < 0.004 && x_left > 0.004){
      velocidadeE = m_speed;
      velocidadeD = (m_speed * sen) * magnitude * -1;
    } 
    else if (y_left < 0.04 && x_left < 0.04){
      velocidadeE = (m_speed * sen) * magnitude * -1;
      velocidadeD = m_speed;
    }
    else if (y_left > 0.04 && x_left < 0.04){
      velocidadeE = -(m_speed * sen) * magnitude;
      velocidadeD = -m_speed;
    }
    else if (y_left > 0.004 & x_left > 0.004){
      velocidadeE = -m_speed;
      velocidadeD = -(m_speed * sen) * magnitude;
    }
    else if (y_left < 0.04 && y_left > -0.04 && x_left < 0.04 && x_left > 0){
      velocidadeE = m_speed * 0;
      velocidadeD = m_speed * 0;
    }
  }
  public void anaD() {

    double magnitude2 = Math.sqrt(y_right * y_right + x_right * x_right);
    double sen2 = y_right/magnitude2;

    Math.max(1, velocidadeD);
    Math.max(1, velocidadeE);

    
    if (y_right < 0.004 && x_right > 0.004){
      velocidadeE = m_speed;
      velocidadeD = (m_speed * sen2) * magnitude2 * -1;
    } 
    else if (y_right < 0.04 && x_right < 0.04){
      velocidadeE = (m_speed * sen2) * magnitude2 * -1;
      velocidadeD = m_speed;
    }
    else if (y_right > 0.04 && x_right < 0.04){
      velocidadeE = -(m_speed * sen2) * magnitude2;
      velocidadeD = -m_speed;
    }
    else if (y_right > 0.004 & x_right > 0.004){
      velocidadeE = -m_speed;
      velocidadeD = -(m_speed * sen2) * magnitude2;
    }
    else if (y_left < 0.04 && y_left > -0.04 && x_left < 0.04 && x_left > 0){
      velocidadeE = m_speed * 0;
      velocidadeD = m_speed * 0;
    }
}
  }
