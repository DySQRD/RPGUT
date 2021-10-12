import java.util.HashMap;

public class Stats {
	HashMap<String, Integer> stats;

	int get(String stat) {
		return stats.get(stat);
	}
}
