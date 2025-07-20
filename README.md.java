package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
 

public class Robot extends TimedRobot {
    private final VictorSPX m_leftDrive = new VictorSPX(1);
    private final VictorSPX m_rightDrive = new VictorSPX(2);
    private final VictorSPX m_leftDrive2 = new VictorSPX(3);
    private final VictorSPX m_rightDrive2 = new VictorSPX(4);

    double velocidadeD;
    double velocidadeL;
    GenericHID m_controller;

    Joystick bob = new Joystick(0);

    double Ltrigger;
    double Rtrigger;

    double m_speed = 1;
    double m_leftSpeed = 0.5;
    double m_rightSpeed = 0.5;
    int POV;

    boolean a;
    boolean b;
    boolean x;


  public Robot() {

    m_rightDrive.configNeutralDeadband(0.04);
    m_rightDrive2.configNeutralDeadband(0.04);
    m_leftDrive.configNeutralDeadband(0.04);
    m_leftDrive2.configNeutralDeadband(0.04);

    m_rightDrive.setNeutralMode(NeutralMode.Brake);
    m_rightDrive2.setNeutralMode(NeutralMode.Brake);
    m_leftDrive.setNeutralMode(NeutralMode.Brake);
    m_leftDrive2.setNeutralMode(NeutralMode.Brake);
  }
      public void button(){
      if (a){
          m_speed = 0.25;
      } 
      else if (b){
          m_speed = 0.5;
      }
      else if (x){
          m_speed = 1;
      }

  }
  public void pov() {
    switch(POV){
      case 0:
        m_leftSpeed = 0.5 ;
        m_rightSpeed = 0.5 ;
        break;
      case 90: 
        m_leftSpeed = 0.5 ;
        m_rightSpeed = -0.5 ;
        break;
      case 180:
        m_leftSpeed = -0.5 ;
        m_rightSpeed = -0.5 ;
        break;
      case 270:
        m_leftSpeed = -0.5 ;
        m_rightSpeed = 0.5 ;
        break;
      default:
        m_leftSpeed = 0;
        m_rightSpeed = 0;


      }
  }
  public void Ltrigger(){
    if (Ltrigger > 0.04){
      m_leftSpeed = Ltrigger * -1;
    }
  }
  public void Rtrigger(){
    if (Rtrigger > 0.04){
      m_rightSpeed = Rtrigger;
    }
  }
     public void Smartdashboard(){
    SmartDashboard.putNumber("RTrigger", Rtrigger);
    SmartDashboard.putNumber("LTrigger", Ltrigger);
    SmartDashboard.putNumber("m_speed", m_speed);
    SmartDashboard.putBoolean("a", a);
    SmartDashboard.putBoolean("b", b);
    SmartDashboard.putBoolean("x", x);
  }
  }

  @Override
  public void teleopPeriodic() {

    Ltrigger = bob.getRawAxis(2);
    Rtrigger = bob.getRawAxis(3);

    a = m_controller.getRawButton(1);
    b = m_controller.getRawButton(2);
    x = m_controller.getRawButton(3);

    velocidadeL = m_speed * m_leftSpeed;
    velocidadeD = m_speed * m_rightSpeed; 

    m_rightDrive.set(ControlMode.PercentOutput, velocidadeD);
    m_rightDrive2.set(ControlMode.PercentOutput, velocidadeD);
    m_leftDrive.set(ControlMode.PercentOutput, velocidadeL);
    m_leftDrive2.set(ControlMode.PercentOutput, velocidadeL);

    if (POV != -1) {
      pov();
    } else {
      velocidadeD = 0;
      velocidadeL = 0;
    }

   button();
   Ltrigger();
   Rtrigger();
   Smartdashboard();
  }
    
 
