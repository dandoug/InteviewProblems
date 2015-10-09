package com.ebay.interview.questions.util;

import org.westminsterkenel.IDog;

public interface DogFactory {
	
	IDog create(String name);
	
	Class<? extends IDog> getDogClass();

}
