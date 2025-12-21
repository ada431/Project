// Main.java — Students version
import java.io.*;
import java.util.*;


public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};
    static int[][][] profits = new int[MONTHS][DAYS][COMMS];
    static boolean loaded = false;


    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        try {
            for (int m = 0; m < MONTHS; m++) {

                File file = new File(months[m] + ".txt");
                Scanner sc = new Scanner(file);

                // başlık satırını atla: Day,Commodity,Profit
                sc.nextLine();

                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] parts = line.split(",");

                    int day = Integer.parseInt(parts[0]) - 1;
                    String commodity = parts[1];
                    int profit = Integer.parseInt(parts[2]);

                    int cIndex = -1;
                    for (int i = 0; i < COMMS; i++) {
                        if (commodities[i].equals(commodity)) {
                            cIndex = i;
                            break;
                        }
                    }

                    if (cIndex != -1) {
                        profits[m][day][cIndex] = profit;
                    }
                }

                sc.close();
            }

            loaded = true;

        } catch (Exception e) {
            loaded = false;
        }
    }
    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }
        int bestIndex = 0;
        int bestTotal = Integer.MIN_VALUE;

        for (int c = 0; c < COMMS; c++) {
            int total = 0;
            for (int d = 0; d < DAYS; d++) {
                total += profits[month][d][c];
            }
            if (total > bestTotal) {
                bestTotal = total;
                bestIndex = c;
            }
        }
        return commodities[bestIndex] + " " + bestTotal;
    }

    public static int totalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) {
            return -99999;
        }
        int sum = 0;
        int dIndex = day - 1;
        for (int c = 0; c < COMMS; c++) {
            sum += profits[month][dIndex][c];
        }
        return sum;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int cIndex = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(commodity)) {
                cIndex = i;
                break;
            }
        }

        if (cIndex == -1) return -99999;
        if (from < 1 || from > DAYS || to < 1 || to > DAYS || from > to) return -99999;

        int total = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = from - 1; d <= to - 1; d++) {
                total += profits[m][d][cIndex];
            }
        }
        return total;
    }

    public static int bestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1;

        int bestDay = 1;
        int bestTotal = Integer.MIN_VALUE;

        for (int day = 1; day <= DAYS; day++) {
            int sum = totalProfitOnDay(month, day);
            if (sum > bestTotal) {
                bestTotal = sum;
                bestDay = day;
            }
        }
        return bestDay;
    }

    public static String bestMonthForCommodity(String comm) {

        int cIndex = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return "INVALID_COMMODITY";

        int bestMonth = 0;
        int bestTotal = Integer.MIN_VALUE;

        for (int m = 0; m < MONTHS; m++) {
            int total = 0;
            for (int d = 0; d < DAYS; d++) {
                total += profits[m][d][cIndex];
            }
            if (total > bestTotal) {
                bestTotal = total;
                bestMonth = m;
            }
        }
        return months[bestMonth];
    }

    public static int consecutiveLossDays(String comm) {

        int cIndex = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return -1;

        int best = 0;
        int current = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profits[m][d][cIndex] < 0) {
                    current++;
                    if (current > best) best = current;
                } else {
                    current = 0;
                }
            }
        }
        return best;
    }

    public static int daysAboveThreshold(String comm, int threshold) {

        int cIndex = -1;
        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(comm)) {
                cIndex = i;
                break;
            }
        }
        if (cIndex == -1) return -1;

        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (profits[m][d][cIndex] > threshold) count++;
            }
        }
        return count;
    }

    public static int biggestDailySwing(int month) {

        if (month < 0 || month >= MONTHS) return -99999;

        int best = 0;
        for (int day = 1; day <= DAYS - 1; day++) {
            int a = totalProfitOnDay(month, day);
            int b = totalProfitOnDay(month, day + 1);

            int diff = a - b;
            if (diff < 0) diff = -diff;

            if (diff > best) best = diff;
        }
        return best;
    }

    public static String compareTwoCommodities(String c1, String c2) {

        int i1 = -1;
        int i2 = -1;

        for (int i = 0; i < COMMS; i++) {
            if (commodities[i].equals(c1)) i1 = i;
            if (commodities[i].equals(c2)) i2 = i;
        }

        if (i1 == -1 || i2 == -1) return "INVALID_COMMODITY";

        int t1 = 0, t2 = 0;

        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                t1 += profits[m][d][i1];
                t2 += profits[m][d][i2];
            }
        }

        if (t1 == t2) return "Equal";
        if (t1 > t2) return c1 + " is better by " + (t1 - t2);
        return c2 + " is better by " + (t2 - t1);
    }

    public static String bestWeekOfMonth(int month) {

        if (!loaded) {
            return "DATA_NOT_LOADED";
        }

        if (month < 0 || month >= MONTHS) {
            return "INVALID_MONTH";
        }

        int bestWeek = 0;
        int maxProfit = Integer.MIN_VALUE;

        for (int w = 0; w < 4; w++) {
            int weekProfit = 0;

            for (int d = w * 7; d < w * 7 + 7; d++) {
                for (int c = 0; c < COMMS; c++) {
                    weekProfit += profits[month][d][c];
                }
            }

            if (weekProfit > maxProfit) {
                maxProfit = weekProfit;
                bestWeek = w;
            }
        }

        return "WEEK_" + (bestWeek + 1);
    }

    public static void main(String[] args) {
        loadData(); System.out.println("Loaded = " + loaded);
        System.out.println("Day 1 January total = " + totalProfitOnDay(0, 1));
        System.out.println("Best week January = " + bestWeekOfMonth(0));
        System.out.println("Data loaded – ready for queries");
    }
}