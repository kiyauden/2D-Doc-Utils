package fr.kiyauden._2ddoc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Utils2dDoc {

    private static final Injector INJECTOR = Guice.createInjector(new LibInjector());

    public static Parser newParser() {
        return INJECTOR.getInstance(Parser.class);
    }

}
