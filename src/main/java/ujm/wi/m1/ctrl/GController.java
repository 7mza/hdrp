package ujm.wi.m1.ctrl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import ujm.wi.m1.algo.Freeman;
import ujm.wi.m1.algo.Knn;
import ujm.wi.m1.algo.Reduce;
import ujm.wi.m1.db.Entry;
import ujm.wi.m1.db.EntryRepo;
import ujm.wi.m1.db.TestResult;
import ujm.wi.m1.db.TestResultRepo;
import ujm.wi.m1.util.Util;

@Controller
@EnableWebMvc
public class GController {
	private static final Logger log = LogManager.getLogger(GController.class.getName());

	@Autowired
	private Util util;

	@Autowired
	private Freeman freeman;

	@Autowired
	private Knn knn;

	@Autowired
	private Reduce reduce;

	@Autowired
	private EntryRepo entryRepo;

	@Autowired
	private TestResultRepo testRepo;
	
	@RequestMapping(value = "/blabla", method = RequestMethod.GET)
	public String blabla() {
		return "blabla";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
		return "test";
	}

	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String stats() {
		return "stats";
	}

	@RequestMapping(value = "/data", method = RequestMethod.GET)
	public String data() {
		return "data";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	public String add(@ModelAttribute(value = "pixels") int[] pixels, @ModelAttribute(value = "digit") int digit,
			@ModelAttribute(value = "test") int test) {
		int[][] matrix = util.arrayToMatrix(pixels, 200);
		String freemanCode = freeman.getFreemanCode(matrix);
		String pixelsString = util.arraytoString(pixels);
		entryRepo.saveAndFlush(new Entry(digit, freemanCode, pixelsString, test == 1));
		return "";
	}

	@RequestMapping(value = "/refresh/data", method = RequestMethod.GET)
	@ResponseBody
	public String refreshData() {
		String json = "{" + jsonify("distEntry") + ":[";
		for (int i = 0; i < 9; i++)
			json = json + entryRepo.countByValueAndTest(i, false) + ",";
		json = json + entryRepo.countByValueAndTest(9, false) + "]," + jsonify("totalEntry") + ":";
		json = json + entryRepo.countByTest(false) + "," + jsonify("distTest") + ":[";
		for (int i = 0; i < 9; i++)
			json = json + entryRepo.countByValueAndTest(i, true) + ",";
		json = json + entryRepo.countByValueAndTest(9, true) + "]," + jsonify("totalTest") + ":";
		json = json + entryRepo.countByTest(true) + "}";
		return json;
	}

	@RequestMapping(value = "/clean/outliers", method = RequestMethod.GET)
	@ResponseBody
	public String cleanOutliers() {
		List<Entry> entries = entryRepo.findAllByTest(false);
		List<Entry> cleaned = reduce.cleanOutliers(entries);
		entryRepo.delete(entries);
		entryRepo.save(cleaned);
		return "";
	}

	@RequestMapping(value = "/clean/irrelevant", method = RequestMethod.GET)
	@ResponseBody
	public String cleanIrrelevant() {
		List<Entry> entries = entryRepo.findAllByTest(false);
		List<Entry> cleaned = reduce.cleanIrrelevant(entries);
		entryRepo.delete(entries);
		entryRepo.save(cleaned);
		return "";
	}

	/*
	 * wecka learning
	 */
	// TODO
	@RequestMapping(value = "/generate/reduce", method = RequestMethod.GET)
	public void generateReduce(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int rate = 10;
		boolean summation = false;
		String content = generateArffHeader(rate);
		content = content + generateArffBody(entries, rate, summation);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/sum", method = RequestMethod.GET)
	public void generateSum(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int rate = 10;
		boolean summation = true;
		String content = generateArffHeader(rate);
		content = content + generateArffBody(entries, rate, summation);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce-sum.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice", method = RequestMethod.GET)
	public void generateSlice(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int slices = 10;
		String content = "@relation is\n";
		for (int i = 1; i <= slices; i++)
			content = content + "@attribute slice" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/norm", method = RequestMethod.GET)
	public void generateSliceNorm(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int slices = 10;
		String content = "@relation is\n";
		for (int i = 1; i <= slices; i++)
			content = content + "@attribute slice" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			double[] norms = util.normalize(parts);
			for (double i : norms)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice-norm.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/hist", method = RequestMethod.GET)
	public void generateHist(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		String content = "@relation is\n";
		for (int i = 0; i < 8; i++)
			content = content + "@attribute h" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.getHistogram(freeman);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("hist.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	/*
	 * Wecka test
	 */
	// TODO
	@RequestMapping(value = "/generate/reduce/test", method = RequestMethod.GET)
	public void generateReduceTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int rate = 10;
		boolean summation = false;
		String content = generateArffHeader(rate);
		content = content + generateArffBody(entries, rate, summation);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce.test.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/sum/test", method = RequestMethod.GET)
	public void generateSumTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int rate = 10;
		boolean summation = true;
		String content = generateArffHeader(rate);
		content = content + generateArffBody(entries, rate, summation);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce-sum.test.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/test", method = RequestMethod.GET)
	public void generateSliceTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int slices = 10;
		String content = "@relation is\n";
		for (int i = 1; i <= slices; i++)
			content = content + "@attribute slice" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice.test.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/norm/test", method = RequestMethod.GET)
	public void generateSliceNormTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int slices = 10;
		String content = "@relation is\n";
		for (int i = 1; i <= slices; i++)
			content = content + "@attribute slice" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			double[] norms = util.normalize(parts);
			for (double i : norms)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice-norm.test.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/hist/test", method = RequestMethod.GET)
	public void generateHistTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		String content = "@relation is\n";
		for (int i = 0; i < 8; i++)
			content = content + "@attribute h" + i + " numeric\n";
		content = content + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.getHistogram(freeman);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("hist.test.arff"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	/*
	 * matlab learning
	 */
	// TODO
	@RequestMapping(value = "/generate/reduce/csv", method = RequestMethod.GET)
	public void generateReduceCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int rate = 10;
		boolean summation = false;
		String content = "";
		for (Entry entry : entries)
			content = content + util.reduceMatrix(entry.getPixels(), rate, summation) + "," + entry.getValue() + "\n";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/sum/csv", method = RequestMethod.GET)
	public void generateSumCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int rate = 10;
		boolean summation = true;
		String content = "";
		for (Entry entry : entries)
			content = content + util.reduceMatrix(entry.getPixels(), rate, summation) + "," + entry.getValue() + "\n";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce-sum.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/csv", method = RequestMethod.GET)
	public void generateSliceCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int slices = 10;
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/norm/csv", method = RequestMethod.GET)
	public void generateSliceNormCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		int slices = 10;
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			double[] norms = util.normalize(parts);
			for (double i : norms)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice-norm.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/hist/csv", method = RequestMethod.GET)
	public void generateHistCSV(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(false);
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.getHistogram(freeman);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("hist.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	/*
	 * matlab test
	 */
	// TODO
	@RequestMapping(value = "/generate/reduce/csv/test", method = RequestMethod.GET)
	public void generateReduceCSVTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int rate = 10;
		boolean summation = false;
		String content = "";
		for (Entry entry : entries)
			content = content + util.reduceMatrix(entry.getPixels(), rate, summation) + "," + entry.getValue() + "\n";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce.test.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/sum/csv/test", method = RequestMethod.GET)
	public void generateSumCSVTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int rate = 10;
		boolean summation = true;
		String content = "";
		for (Entry entry : entries)
			content = content + util.reduceMatrix(entry.getPixels(), rate, summation) + "," + entry.getValue() + "\n";
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("reduce-sum.test.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/csv/test", method = RequestMethod.GET)
	public void generateSliceCSVTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int slices = 10;
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice.test.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/slice/norm/csv/test", method = RequestMethod.GET)
	public void generateSliceNormCSVTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		int slices = 10;
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.sliceFreeman(freeman, slices);
			double[] norms = util.normalize(parts);
			for (double i : norms)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("slice-norm.test.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	// TODO
	@RequestMapping(value = "/generate/hist/csv/test", method = RequestMethod.GET)
	public void generateHistCSVTest(HttpServletResponse response) throws FileNotFoundException, IOException {
		List<Entry> entries = entryRepo.findAllByTest(true);
		String content = "";
		for (Entry entry : entries) {
			String freeman = entry.getFreeman();
			int[] parts = util.getHistogram(freeman);
			for (int i : parts)
				content = content + i + ",";
			content = content + entry.getValue() + "\n";
		}
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + jsonify("hist.test.csv"));
		IOUtils.write(content, response.getOutputStream());
		response.flushBuffer();
	}

	/*
	 * 
	 */
	@RequestMapping(value = "/classify", method = RequestMethod.POST)
	@ResponseBody
	public String classify(@ModelAttribute(value = "pixels") int[] pixels, @ModelAttribute(value = "algo") int algo) {
		int num = 0;
		int[][] matrix = util.arrayToMatrix(pixels, 200);
		String freemanCode = freeman.getFreemanCode(matrix);
		List<Entry> entries = entryRepo.findAllByTest(false);
		num = knn.classify(freemanCode, entries, algo);
		log.info("input classification : " + num);
		return "" + num;
	}

	@RequestMapping(value = "/test/batch", method = RequestMethod.GET)
	@ResponseBody
	public String batch() {
		List<Entry> entries = entryRepo.findAllByTest(false);
		List<Entry> tests = entryRepo.findAllByTest(true);
		List<TestResult> results = testBatch(tests, entries, TestResult.Algo.knn3);
		testRepo.save(results);
		results = testBatch(tests, entries, TestResult.Algo.knn5);
		testRepo.save(results);
		results = testBatch(tests, entries, TestResult.Algo.knn7);
		testRepo.save(results);
		return "";
	}

	@RequestMapping(value = "/refresh/stats", method = RequestMethod.GET)
	@ResponseBody
	public String refreshStats() {
		String json = "{" + jsonify("data3nn") + ":[[";
		for (int i = 0; i < 9; i++)
			json = json + testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn3) + ",";
		json = json + testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn3) + "],[";
		for (int i = 0; i < 9; i++)
			json = json + (testRepo.countByValueAndAlgo(i, TestResult.Algo.knn3)
					- testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn3)) + ",";
		json = json + (testRepo.countByValueAndAlgo(9, TestResult.Algo.knn3)
				- testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn3)) + "]],";
		//
		json = json + jsonify("data5nn") + ":[[";
		for (int i = 0; i < 9; i++)
			json = json + testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn5) + ",";
		json = json + testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn5) + "],[";
		for (int i = 0; i < 9; i++)
			json = json + (testRepo.countByValueAndAlgo(i, TestResult.Algo.knn5)
					- testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn5)) + ",";
		json = json + (testRepo.countByValueAndAlgo(9, TestResult.Algo.knn5)
				- testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn5)) + "]],";
		//
		json = json + jsonify("data7nn") + ":[[";
		for (int i = 0; i < 9; i++)
			json = json + testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn7) + ",";
		json = json + testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn7) + "],[";
		for (int i = 0; i < 9; i++)
			json = json + (testRepo.countByValueAndAlgo(i, TestResult.Algo.knn7)
					- testRepo.countByValueAndGuessAndAlgo(i, i, TestResult.Algo.knn7)) + ",";
		json = json + (testRepo.countByValueAndAlgo(9, TestResult.Algo.knn7)
				- testRepo.countByValueAndGuessAndAlgo(9, 9, TestResult.Algo.knn7)) + "]]";
		json = json + "}";
		log.info(json);
		return json;
	}

	private String generateArffHeader(int rate) {
		String header = "@relation is\n";
		for (int i = 1; i <= (200 / rate) * (200 / rate); i++)
			header = header + "@attribute pixel" + i + " numeric\n";
		header = header + "@attribute digit {0,1,2,3,4,5,6,7,8,9}\n@data\n";
		return header;
	}

	private String generateArffBody(List<Entry> entries, int rate, boolean summation) {
		String body = "";
		for (Entry entry : entries)
			body = body + util.reduceMatrix(entry.getPixels(), rate, summation) + "," + entry.getValue() + "\n";
		return body;
	}

	private List<TestResult> testBatch(List<Entry> tests, List<Entry> entries, TestResult.Algo algo) {
		log.info(algo + " : testing " + tests.size() + " inputs");
		List<TestResult> results = new ArrayList<>();
		Long start = System.currentTimeMillis();
		for (Entry test : tests) {
			int guess = knn.classify(test.getFreeman(), entries, algo.getId());
			results.add(new TestResult(algo, test.getValue(), guess));
		}
		Long duration = System.currentTimeMillis() - start;
		log.info(duration / 1000 + " s to complete");
		return results;
	}

	private String jsonify(String str) {
		return "\"" + str + "\"";
	}

}