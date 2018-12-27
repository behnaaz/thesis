import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;
public class Test {
    public static void main(final String... args) {
	class Node { 
		List<Node> getNexts() { return new ArrayList<Node>(); }
	}
        class Gateway extends Node {}	    
        class ForkGateway extends Gateway {}	    
	class Event extends Node {}
	class SendMessageEvent extends Event {}
	class ReceiveMessageEvent extends Event {}
	class Task extends Node {
		final String name;
		Task(final String name) {this.name = name;}
		String getName() { return name; }
		boolean isCompensible() { return new Random().nextBoolean(); }
		Task getCompensation() { return new Task("COMP"+new Random().nextInt()); }
		List<Event> getCancelEvents() { List<Event> el = new ArrayList<Event>(); el.add(new Event()); return el; }
	}
	class Transaction extends Node {
		final List<Task> tasks;
		Transaction(final List<Task> tasks) { this.tasks = tasks; }
		List<Task> getTasks() {	return tasks; }
	}
	class BPMN {
		final List<Transaction> transactions;
		BPMN(final List<Transaction> transactions){ this.transactions = transactions; }
		List<Transaction> getTransactions() { return transactions; }
		final void createMessageEvent(final Task t, final ListIterator i) {
			Task k = new Task("BBBB"+new Random().nextInt());
			i.add(k);
			System.out.println("create message event");

			//Create a message event to indicates the task is done
 			Event doneSendEvent = new SendMessageEvent();
  			i.add(doneSendEvent);
			  //Place the message event after the task
  			if (k.getNexts().isEmpty()) {
			    k.getNexts().add(doneSendEvent);
 			} else {
    				Gateway gf = new ForkGateway();
    				i.add(gf);
			        gf.getNexts().addAll(k.getNexts());
    				gf.getNexts().forEach(g ->
      					g.getNexts().add(doneSendEvent)
				);
			    k.getNexts().add(gf);
  
		        Event doneReceiveEvent = new ReceiveMessageEvent();
  			i.add(doneReceiveEvent);
  			//doneCmpnsRcvMsgEvs 
			//////receiveCompensatesDone.get(k) = doneSendEvent;
		        // Indicating end of compensation
  			Event compensationDoneSendEvent = new SendMessageEvent();
  			i.add(compensationDoneSendEvent);
  			Event compensationDoneReceiveEvent = new ReceiveMessageEvent();
  //receiveCompensatesDone.get(k) = compensationDoneReceiveEvent;
  i.add(compensationDoneReceiveEvent);
  k.getCompensation().getNexts().add(compensationDoneSendEvent);
}


		}
		final void handleCancelEvent(final Event v, final Transaction x, final Task t) {
			System.out.println("handle cancel event");
		}
	}

	 List<Task> l = new ArrayList<>();
         l.add(new Task("s"));
         l.add(new Task("r"));
	 l.add(new Task("d"));

	Transaction t = new Transaction(l);
	List<Transaction> n = new ArrayList<>();
	n.add(t);

	BPMN b = new BPMN(n);
        
	Consumer<BPMN> print = bpmn -> {	
		bpmn.getTransactions().forEach(e -> System.out.println(e.getTasks().size()));
		bpmn.getTransactions().forEach(e -> e.getTasks().stream().filter(f -> !"Z".equals(f.getName())).forEach(f -> System.out.println(f.getName())));
		System.out.println("---------------------------------------------------------");
    	};


    print.accept(b);

	ListIterator<Transaction> ix = b.getTransactions().listIterator(); 
	while (ix.hasNext()) {
		Transaction x = ix.next();
		ListIterator<Task> ik = t.getTasks().listIterator();
		while (ik.hasNext()) {
			Task k = ik.next();
			if (k.isCompensible()) {
          			b.createMessageEvent(k, ik);
				ListIterator<Event> iv = k.getCancelEvents().listIterator(); 
				while (iv.hasNext()) {
					Event v = iv.next();
             				b.handleCancelEvent(v, x, k.getCompensation());
				}
			}
		}	

	}

    print.accept(b);
  }
}
