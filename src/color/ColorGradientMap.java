package color;

import java.awt.Color;
import java.util.TreeMap;
/**
 * Defines a linearly-interpolated mapping from a range of numerical values to a color gradient
 * 
 *
 */
public class ColorGradientMap {
	TreeMap<Double,Color> values = new TreeMap<Double,Color>();
	
	public ColorGradientMap() {}
	
	public void addColor(double value, Color color) {
		values.put(value, color);
	}
	
	public Color getColor(double value) {
		if (values.isEmpty())
			return Color.WHITE;
		
		if (value <= values.firstKey()) {
			return values.firstEntry().getValue();
		}
		if (value >= values.lastKey())
			return values.lastEntry().getValue();
		
		Color color1 = values.floorEntry(value).getValue();
		Color color2 = values.ceilingEntry(value).getValue();
		double ratio = (value - values.floorKey(value))/(values.ceilingKey(value)-values.floorKey(value));
		
		if (Double.isNaN(ratio))
			return color1;
		
		int r1 = color1.getRed(); int r2 = color2.getRed();
		int b1 = color1.getBlue(); int b2 = color2.getBlue();
		int g1 = color1.getGreen(); int g2 = color2.getGreen();
		
		
		return new Color(avgColorComp(r1,r2,ratio),avgColorComp(g1,g2,ratio),avgColorComp(b1,b2,ratio));
	}
	
	/**
	 * Finds the weighted average of two color components (for example, red green or blue)
	 * @param c1 an integer from 0-255
	 * @param c2 an integer from 0-255
	 * @param r the ratio 0-1 for the weighed average
	 * @return the averaged color component
	 */
	private int avgColorComp(int c1, int c2, double r) {
		return (int) Math.sqrt(c1*c1*(1-r)+c2*c2*r);
		}
}
