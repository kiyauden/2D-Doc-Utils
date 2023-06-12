package fr.kiyauden._2ddoc;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

class LibInjector extends AbstractModule {

    @Override
    protected void configure() {

        // Formatters
        final Multibinder<DataParser<?>> formatterMultibinder =
                Multibinder.newSetBinder(binder(), new TypeLiteral<DataParser<?>>() {
                });
        formatterMultibinder.addBinding().to(AmountDataParser.class);
        formatterMultibinder.addBinding().to(BooleanDataParser.class);
        formatterMultibinder.addBinding().to(DateDataParser.class);
        formatterMultibinder.addBinding().to(IntegerDataParser.class);
        formatterMultibinder.addBinding().to(TextDataParser.class);
        formatterMultibinder.addBinding().to(URLDataParser.class);
        formatterMultibinder.addBinding().to(TimeDataParser.class);

        // Services
        bind(IDataService.class).to(DataService.class);
        bind(IDocumentService.class).to(DocumentService.class);
        bind(IHeaderService.class).to(HeaderService.class);
        bind(IParserService.class).to(ParserService.class);
        bind(ISignatureService.class).to(SignatureService.class);
        bind(Parser.class).to(DefaultParser.class);

    }

}


