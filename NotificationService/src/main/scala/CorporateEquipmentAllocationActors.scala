import akka.actor.{Actor, ActorRef}
import models.KafkaMessageFormat


class ManagerApprovalMessageListener() extends Actor {


  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Manager Approval Message Listener consumes the message")
  }
}

class InventoryMessageListener() extends Actor {

  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Inventory Message Listener consumes the message")
      EmailUtils.sendEmail("yaseenbaba12345@gmail.com", msg.messageType, msg.message)

  }
}

class MaintenanceMessageListener() extends Actor {

  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      println("Maintenance Message Listener consumes the message")
      EmailUtils.sendEmail("yaseenbaba12345@gmail.com", msg.messageType, msg.message)
  }
}

class EmployeeMessageListener() extends Actor {

  override def receive: Receive = {
    case msg: KafkaMessageFormat =>
      if(msg.messageType == "OVERDUE_NOTIFICATION"){
        println("OverDue Notification Message Listener consumes the message")
        EmailUtils.sendEmail(msg.receiver, msg.messageType, msg.message)
      }
      else {
        println("Employee Message Listener consumes the message")
        EmailUtils.sendEmail(msg.receiver, msg.messageType, msg.message)
      }
  }
}

class CorporateEquipAllocation(managerApprovalMessageListener: ActorRef,
                               inventoryMessageListener: ActorRef,
                               maintenanceMessageListener: ActorRef,
                               employeeMessageListener: ActorRef
                              )extends Actor {
  override def receive: Receive = {
    case msg: KafkaMessageFormat => msg.receiver match {
      case "MANAGER" =>
        managerApprovalMessageListener ! msg
      case "INVENTORY" =>
        inventoryMessageListener ! msg
      case "MAINTENANCE" =>
        maintenanceMessageListener ! msg
      case "EMPLOYEE" =>
        employeeMessageListener ! msg
    }
  }

}
