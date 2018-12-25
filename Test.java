import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.List;
public class Test {
    public static void main(final String... args) {
	class Task {
		final String name;
		Task(final String name) {this.name = name;}
		String getName() { return name; }
		boolean isCompensible() { return new Random().nextBoolean(); }
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
       Function<BPMN, BPMN> refine = p -> {
  p.getTransactions()
   .forEach(x ->
      x.getTasks()
       .stream()
       .filter(a -> a.isCompensible())
       .forEach(k -> {
    //      createMessageEvents(t, x);//???
      //    t.getCancelEvents()
          // .forEach(v -> 
           System.out.println(k); //   handleCancelEvent(v, x, a.compensation, ts)
        })
    ); return p;};

    print.accept(b);
    BPMN c = refine.apply(b);
    print.accept(c);
  }
}

