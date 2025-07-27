package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.PS4Controller.Button;
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
    double velocidadeL;

    Joystick bob = new Joystick(0);

    double Ltrigger;
    double Rtrigger;


    double x_left;
    double x_right;
    double y_left;
    double y_right;

    double m_speed = 0;
    int POV;

    boolean a;
    boolean b;
    boolean x;

    double magnitude = Math.hypot(x_left, y_left);

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

    anaE();

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

    m_rightDrive.set(ControlMode.PercentOutput, velocidadeD);
    m_rightDrive2.set(ControlMode.PercentOutput, velocidadeD);
    m_leftDrive.set(ControlMode.PercentOutput, velocidadeL);
    m_leftDrive2.set(ControlMode.PercentOutput, velocidadeL);
  }

  public void dash(){
    SmartDashboard.putNumber("RTrigger", Rtrigger);
    SmartDashboard.putNumber("LTrigger", Ltrigger);
    SmartDashboard.putNumber("m_speed", m_speed);
    SmartDashboard.putNumber("velocidadeD", velocidadeD);
    SmartDashboard.putNumber("VelocidadeL", velocidadeL);
    SmartDashboard.putBoolean("a", a);
    SmartDashboard.putBoolean("b", b);
    SmartDashboard.putBoolean("x", x);

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
        velocidadeL = m_speed;
        velocidadeD = m_speed;
        break;
      case 90: 
        velocidadeL = m_speed;
        velocidadeD = -m_speed;
        break;
      case 180:
        velocidadeL = -m_speed;
        velocidadeD = -m_speed;
        break;
      case 270:
        velocidadeL = -m_speed;
        velocidadeD = m_speed;
        break;
      case -1:
        velocidadeL = 0;
        velocidadeD = 0;
      }
  }
 
  public void Ltrigger(){
    if (Ltrigger > 0.04){
      velocidadeL = Ltrigger * -m_speed;
      velocidadeD = Ltrigger * -m_speed;
    } else{
      velocidadeL = m_speed * 0;
      velocidadeD = m_speed * 0;
    }
  }

  public void Rtrigger(){
    if (Rtrigger > 0.04){
      velocidadeD = Rtrigger * m_speed;
      velocidadeL = Rtrigger * m_speed;
    } else{
      velocidadeD = m_speed * 0;
      velocidadeL = m_speed * 0;
    }
  }

  public void anaE() {
    if (y_left > 0 & x_left > 0){ //primeiro quadrante
      velocidadeD = magnitude * m_speed - 1;
      velocidadeL = magnitude * m_speed + 1;
      }
    else if (y_left > 0 & x_left < 0){ //segundo quadrante
      velocidadeD = magnitude * m_speed + 1;
      velocidadeL = magnitude * m_speed - 1;
      }
    else if (y_left < 0 & x_left < 0){ //terceiro quadrante
      velocidadeD = magnitude * m_speed - 1;
      velocidadeL = magnitude * m_speed + 1;
      }
    else if (y_left < 0 & x_left > 0){ //quarto quadrante
      velocidadeD = magnitude * m_speed + 1;
      velocidadeL = magnitude * m_speed - 1;
      }
    }
  // public void anaD(){
  //   if (y_right > 0.04 & x_right > 0){ //primeiro quadrante

  //   }
  //   else if (y_right > 0 & x_right < 0){ //segundo quadrante

  //   }
  //   else if (y_right < 0 & x_right < 0){ //terceiro quadrante

  //   }
  //   else if (y_right < 0 & x_right > 0){ //quarto quadrante

  //   }
  }
