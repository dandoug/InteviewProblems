package org.ken.sorting;

import org.junit.Before;
import org.junit.Test;
import org.westminsterkenel.IDog;

import com.ebay.interview.questions.util.DogFactory;
import com.ebay.interview.questions.util.GenericTests;

public class DogArrangingTest {

	DogFactory df;
	
	@Before
	public void init() {
		df = new DogFactory() {
			@Override
			public IDog create(String name) {
				return new Dog(name);
			}
			@Override
			public Class<? extends IDog> getDogClass() {
				return Dog.class;
			}
		};
	}

	@Test
	public void test() {
		GenericTests.testPlain(df, DogSortUtility.getInstance());
	}
	

	@Test(expected=IllegalStateException.class)
	public void testWithCycle() {
		GenericTests.testWithCycle(df, DogSortUtility.getInstance());
	}	
	
	@Test
	public void testWithRandomDataAndVisualization() {
		GenericTests.testWithRandomDataAndVisualization(df, DogSortUtility.getInstance());
	}	
	
}
