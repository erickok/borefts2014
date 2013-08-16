package nl.brouwerijdemolen.borefts2013.api;

public class Style implements Comparable<Style> {

	private int id;
	private String name;
	private int color;
	private int abv;
	private int bitterness;
	private int sweetness;
	private int acidity;

	public Style() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getColor() {
		return color;
	}

	public int getAbv() {
		return abv;
	}

	public int getBitterness() {
		return bitterness;
	}

	public int getSweetness() {
		return sweetness;
	}

	public int getAcidity() {
		return acidity;
	}

	@Override
	public int compareTo(Style another) {
		return name.compareTo(another.getName());
	}

}
