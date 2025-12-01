import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Backtester {

    // --- 1. 参数设置 ---
    private static final String CSV_FILE = "aapl_data.csv";
    private static final int SHORT_WINDOW = 10;
    private static final int LONG_WINDOW = 250;
    private static final double INITIAL_CAPITAL = 100000.0;

    public static void main(String[] args) {
        // --- 2. 加载并解析数据 ---
        List<Bar> bars = loadData(CSV_FILE);
        if (bars.isEmpty()) {
            System.err.println("无法加载数据或数据为空。");
            return;
        }

        // --- 3. 计算技术指标和生成信号 ---
        calculateIndicatorsAndSignals(bars);

        // --- 4. 执行回测 ---
        runBacktest(bars);
    }

    /**
     * 从 CSV 文件中读取数据并解析为 Bar 列表
     */
    private static List<Bar> loadData(String filename) {
        List<Bar> bars = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // 跳过表头

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                LocalDate date = LocalDate.parse(values[0], formatter);
                double closePrice = Double.parseDouble(values[4]);
                bars.add(new Bar(date, closePrice));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bars;
    }

    /**
     * 计算均线和交易信号
     */
    private static void calculateIndicatorsAndSignals(List<Bar> bars) {
        // 首先，计算所有日期的均线
        for (int i = 0; i < bars.size(); i++) {
            Bar currentBar = bars.get(i);

            // 计算短期均线
            if (i + 1 >= SHORT_WINDOW) {
                double sum = 0.0;
                for (int j = 0; j < SHORT_WINDOW; j++) {
                    sum += bars.get(i - j).closePrice;
                }
                currentBar.smaShort = sum / SHORT_WINDOW;
            }

            // 计算长期均线
            if (i + 1 >= LONG_WINDOW) {
                double sum = 0.0;
                for (int j = 0; j < LONG_WINDOW; j++) {
                    sum += bars.get(i - j).closePrice;
                }
                currentBar.smaLong = sum / LONG_WINDOW;
            }
        }

        // 其次，根据均线生成信号 (从长期窗口期后开始判断，避免早期无效信号)
        for (int i = LONG_WINDOW; i < bars.size(); i++) {
            Bar today = bars.get(i);
            Bar yesterday = bars.get(i - 1);

            // 金叉：今天短期线上穿长期线
            if (today.smaShort > today.smaLong && yesterday.smaShort <= yesterday.smaLong) {
                today.signal = 1; // 买入信号
            }
            // 死叉：今天短期线下穿长期线
            else if (today.smaShort < today.smaLong && yesterday.smaShort >= yesterday.smaLong) {
                today.signal = -1; // 卖出信号
            }
        }
    }

    /**
     * 执行回测模拟
     */
    private static void runBacktest(List<Bar> bars) {
        double cash = INITIAL_CAPITAL;
        double shares = 0.0;

        for (Bar bar : bars) {
            // 检查买入信号
            if (bar.signal == 1 && cash > 0) {
                shares = cash / bar.closePrice;
                cash = 0;
                System.out.printf("%s: 买入信号 @ %.2f, 持仓: %.2f 股\n", bar.date, bar.closePrice, shares);
            }
            // 检查卖出信号
            else if (bar.signal == -1 && shares > 0) {
                cash = shares * bar.closePrice;
                shares = 0;
                System.out.printf("%s: 卖出信号 @ %.2f, 资金: %.2f\n", bar.date, bar.closePrice, cash);
            }
        }

        // --- 5. 打印结果 ---
        double finalValue = cash;
        if (shares > 0) {
            finalValue += shares * bars.get(bars.size() - 1).closePrice;
        }

        double totalReturn = (finalValue / INITIAL_CAPITAL - 1) * 100;
        double benchmarkReturn = (bars.get(bars.size() - 1).closePrice / bars.get(0).closePrice - 1) * 100;

        System.out.println("\n--- Java 语言回测结果 ---");
        System.out.printf("初始资金: %.2f\n", INITIAL_CAPITAL);
        System.out.printf("最终资产: %.2f\n", finalValue);
        System.out.printf("策略总收益率: %.2f%%\n", totalReturn);
        System.out.printf("基准(买入持有)收益率: %.2f%%\n", benchmarkReturn);
    }

    /**
     * 内部类，用于存储每日的行情数据和计算出的指标
     */
    static class Bar {
        LocalDate date;
        double closePrice;
        double smaShort;
        double smaLong;
        int signal = 0; // -1: 卖出, 0: 持有, 1: 买入

        public Bar(LocalDate date, double closePrice) {
            this.date = date;
            this.closePrice = closePrice;
        }

        @Override
        public String toString() {
            return String.format("Date: %s, Close: %.2f, SMA_short: %.2f, SMA_long: %.2f, Signal: %d",
                    date, closePrice, smaShort, smaLong, signal);
        }
    }
}