package zosma.model;

import java.util.Comparator;

public class SortDay implements Comparator<Day> {
	
	@Override
    public int compare(Day a, Day b) {
        return a.date.compareTo(b.date);
    }
}
