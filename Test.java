import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;
public class Test {
    public static void main(final String... args) {
	class Event {}
	class Task {
		final String name;
		Task(final String name) {this.name = name;}
		String getName() { return name; }
		boolean isCompensible() { return new Random().nextBoolean(); }
		Task getCompensation() { return new Task("COMP"+new Random().nextInt()); }
		List<Event> getCancelEvents() { List<Event> el = new ArrayList<Event>(); el.add(new Event()); return el; }
	}
	class Transaction {
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
