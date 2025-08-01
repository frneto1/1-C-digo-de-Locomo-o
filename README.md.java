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

    a = bob.getRawButton(1);
    b = bob.getRawButton(2);
    x = bob.getRawButton(3);

    if (POV != -1){
      pov();
    } else {
      if (Rtrigger < 0.04){
        Ltrigger();
      } else if (Ltrigger < 0.04){
        Rtrigger();
      }
    }

      anaE();


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
    

    if (y_left == -1){
      velocidadeE = m_speed;
      velocidadeD = m_speed;
    }
    else if (y_left == 1){
      velocidadeE = -m_speed;
      velocidadeD = -m_speed;
    }
    else if (y_left < 0 && x_left > 0){
      velocidadeE = -(2 * (sen * magnitude - 1) * m_speed);
      velocidadeD = - (2 * (sen * magnitude - (1 * m_speed)) * m_speed);
    } 
    else if (y_left < 0 && x_left < 0){
      velocidadeE = -(2 * (sen * magnitude - (1 * m_speed)) * m_speed);
      velocidadeD = -(2 * (sen * magnitude - 1) * m_speed);
    }
    else if (y_left > 0 && x_left < 0){
      velocidadeE = -(2 * (sen * magnitude + (1 * m_speed)) * m_speed);
      velocidadeD = -(2 * (sen * magnitude + 1) * m_speed);
    }
    else if (y_left > 0 & x_left > 0){
      velocidadeE = -(2 * (sen * magnitude + 1) * m_speed);
      velocidadeD = -(2 * (sen * magnitude + (1 * m_speed)) * m_speed);
    }
    else {
      velocidadeE = 0;
      velocidadeD = 0;
    }
  }
}
