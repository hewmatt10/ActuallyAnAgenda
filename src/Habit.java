import java.util.Calendar;
import java.util.TimeZone;

public interface Habit
{
	void updateEventCompletion(ScheduleEvent event, double completionPercentage);
}

class TimeFrameProductivity implements Habit
{
	// Hour in UTC: (System.currentTimeMillis() / 3600000.0) % (24)
	private double bestWorkingTime;
	public TimeFrameProductivity()
	{
		Calendar now = Calendar.getInstance();
		TimeZone timeZone = now.getTimeZone();
		bestWorkingTime = 13.0 - timeZone.getOffset(System.currentTimeMillis()) / 3600000; // 1 PM
	}
	@Override
	public void updateEventCompletion(ScheduleEvent event, double completionPercentage)
	{
		double eventTimeNeeded = event.getAssociatedTask().getTime();
		double timeWorkedOn = (event.getEnd().getTime() - event.getBegin().getTime()) / (60.0 * 60 * 1000);
		double expectedPercentageCompleted = eventTimeNeeded / timeWorkedOn;
		double mid = ((event.getEnd().getTime() + event.getBegin().getTime()) / 2.0) / 3600000.0 % (24);
		if(completionPercentage > expectedPercentageCompleted * 1.5) bestWorkingTime = mid;
	}
	public static double findHour(long epoch)
	{
		return epoch / 3600000.0 % 24;
	}

	public double getBestWorkingTime()
	{
		return bestWorkingTime;
	}

	public void setBestWorkingTime(double bestWorkingTime)
	{
		this.bestWorkingTime = bestWorkingTime;
	}
}
class ExtendedWorkDuration implements Habit
{
	// Should be approximations.
	private double maxHoursWork, minHoursBreak;
	public ExtendedWorkDuration()
	{
		// Default Values
		maxHoursWork = 1;
		minHoursBreak = 0.5;
	}
	@Override
	public void updateEventCompletion(ScheduleEvent event, double completionPercentage)
	{
		// To Implement
	}

	public double getMaxHoursWork()
	{
		return maxHoursWork;
	}

	public void setMaxHoursWork(double maxHoursWork)
	{
		this.maxHoursWork = maxHoursWork;
	}

	public double getMinHoursBreak()
	{
		return minHoursBreak;
	}

	public void setMinHoursBreak(double minHoursBreak)
	{
		this.minHoursBreak = minHoursBreak;
	}
}
