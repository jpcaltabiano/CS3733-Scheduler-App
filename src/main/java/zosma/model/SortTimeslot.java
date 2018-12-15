package zosma.model;

import java.util.Comparator;

public class SortTimeslot implements Comparator<Timeslot > {
	
	@Override
    public int compare(Timeslot  a, Timeslot  b) {
        return a.time.compareTo(b.time);
    }
}
