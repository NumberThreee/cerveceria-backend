package backend.config;

import backend.model.Empleado;
import backend.model.Producto;
import backend.repository.EmpleadoRepository;
import backend.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductoRepository productoRepository;
    private final EmpleadoRepository empleadoRepository;

    public DataLoader(
            ProductoRepository productoRepository,
            EmpleadoRepository empleadoRepository
    ) {
        this.productoRepository = productoRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public void run(String... args) {
        /*
         * H2 está configurada como base en memoria, por lo que se carga
         * nuevamente al reiniciar la aplicación.
         */
        if (productoRepository.count() == 0) {
            // Cervezas con imagen. Las URLs se reemplazan después por las de Cloudinary.
            producto("Golden Ale", 8000, "cerveza",
                    "Cerveza rubia artesanal",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/golden-ale.jpg");

            producto("Red Ale", 8000, "cerveza",
                    "Cerveza roja artesanal",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176418/red-ale.jpg");

            producto("Hazi IPA", 8000, "cerveza",
                    "IPA artesanal",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/hazi-ipa.jpg");

            producto("Barley Wine", 8000, "cerveza",
                    "Cerveza intensa de alta graduación",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/barley-wine.jpg");

            producto("Porter", 8000, "cerveza",
                    "Cerveza oscura con notas tostadas",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176418/porter.jpg");

            producto("IPA", 8000, "cerveza",
                    "India Pale Ale artesanal",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/ipa.jpg");

            producto("APA", 8000, "cerveza",
                    "American Pale Ale artesanal",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/apa.jpg");

            producto("Honey", 8000, "cerveza",
                    "Cerveza artesanal con miel",
                    "https://res.cloudinary.com/ywehoory/image/upload/v1790176417/honey.jpg");

            // Tragos con cerveza
            producto("Jameson Wheat", 13000, "tragos_con_cerveza",
                    "American, jengibre, almíbar y limón",
                    null);

            producto("IBU", 13000, "tragos_con_cerveza",
                    "IPA, pomelo, almíbar y Cynar",
                    null);

            producto("La Havana Ale", 13000, "tragos_con_cerveza",
                    "Golden, piña, lima, almíbar y Havana Club",
                    null);

            // Sin alcohol
            producto("Mix Frutal", 10000, "sin_alcohol",
                    "Lima, azúcar, maracuyá, naranja y frutos rojos",
                    null);

            producto("Special Lemonade", 8000, "sin_alcohol",
                    "Limonada tradicional con almíbar de flores de hibisco",
                    null);

            producto("Vasuyveda", 10000, "sin_alcohol",
                    "Naranja, limón, azúcar, jengibre y albahaca",
                    null);

            // Negroni
            producto("Negroni Clásico", 13000, "negroni",
                    "Gin, Campari y vermouth rosso",
                    null);

            producto("Negroni Sbagliato", 13000, "negroni",
                    "Campari, vermouth rosso y espumante",
                    null);

            producto("Coffee Negroni", 13000, "negroni",
                    "Gin, licor de café y Campari",
                    null);

            producto("Boulevardier", 15000, "negroni",
                    "Whisky, Campari y vermouth rosso",
                    null);

            // Sour
            producto("Cynar Sour", 12000, "sour",
                    "Cynar, almíbar simple, jugo de lima y clara de huevo",
                    null);

            producto("Pisco Sour", 13000, "sour",
                    "Pisco Capel, jugo de lima, almíbar simple y clara de huevo",
                    null);

            producto("New York Sour", 15000, "sour",
                    "Whisky americano, jugo de lima, almíbar simple, clara de huevo y Malbec",
                    null);

            producto("Jungle Boogie", 15000, "sour",
                    "Whisky Jameson, jugo de lima, almíbar simple, pera macerada y clara de huevo",
                    null);

            producto("Malibu Sour", 15000, "sour",
                    "Malibu, maracuyá, jugo de lima y clara de huevo",
                    null);

            // Cocktails de la casa
            producto("Selva Negra", 13000, "cocktails_casa",
                    "Absolut Vainilla, Cynar, lima, azúcar y top lima-limón",
                    null);

            producto("Citric Chinese", 13000, "cocktails_casa",
                    "Aconcagua lime, lima, almíbar simple y fortune cookie",
                    null);

            producto("Head of Damon", 15000, "cocktails_casa",
                    "Damonjag, lima, almíbar de canela, pomelo y frutos rojos",
                    null);

            // Tiki
            producto("Mai Tai", 17000, "tiki",
                    "Lima, almíbar de miel, almíbar simple, Cointreau, ron Havana y pasta de almendras",
                    null);

            producto("Applemeister", 17000, "tiki",
                    "Lima, almíbar simple, Aquarius de manzana, Jägermeister y menta",
                    null);

            producto("Singapure", 18000, "tiki",
                    "Lima, almíbar simple, Beefeater 24, ananá, Cointreau, cerezas, Tía María y Angostura",
                    null);

            // Clásicos
            producto("Old Fashioned", 13000, "clasicos",
                    "Red Label, almíbar de miel, jengibre y limón",
                    null);

            producto("Margarita", 13000, "clasicos",
                    "José Cuervo Silver, triple sec y lima",
                    null);

            producto("Penicillin", 15000, "clasicos",
                    "Azúcar, Angostura, whisky y piel de naranja",
                    null);

            // Vinos
            producto("Dilema Dulce Natural", 21000, "vinos", null, null);
            producto("Chenin Dulce", 24000, "vinos", null, null);
            producto("Emilia Rosé", 21000, "vinos", null, null);
            producto("Trumpeter Doux Dulce", 33000, "vinos", null, null);

            // Espumantes y otros
            producto("Personal Chandon", 16000, "espumantes", null, null);
            producto("Mumm Extra Brut", 29000, "espumantes", null, null);
            producto("Chandon Extra Brut", 39000, "espumantes", null, null);
            producto("Chandon Brut Nature", 43000, "espumantes", null, null);
            producto("Chandon Delice", 40000, "espumantes", null, null);
            producto("Baron B Extra Brut", 67000, "espumantes", null, null);
            producto("Baron B Nature", 83000, "espumantes", null, null);
            producto("Pommery", 260000, "espumantes", null, null);
            producto("Red Bull x250", 6500, "espumantes", null, null);
            producto("Speed x500", 6000, "espumantes", null, null);
            producto("Speed x250", 4500, "espumantes", null, null);
            producto("Medida Granadina chica", 2000, "espumantes", null, null);
            producto("Medida Granadina grande", 4000, "espumantes", null, null);

            // Vodka
            producto("Beluga", 17000, "vodka", null, null);
            producto("Zubrowka", 16000, "vodka", null, null);
            producto("Absolut", 15000, "vodka", null, null);
            producto("Skyy", 12000, "vodka", null, null);
            producto("Smirnoff", 12000, "vodka", null, null);
            producto("Vodka Speed", 11000, "vodka", null, null);
            producto("Vodka Orange", 11000, "vodka", null, null);
            producto("Smirnoff Lata", 11000, "vodka", null, null);

            // Ron
            producto("Habana 3 Años", 11000, "ron", null, null);
            producto("Habana Especial", 12000, "ron", null, null);
            producto("Habana 7 Años", 15000, "ron", null, null);
            producto("Santa Teresa Añejo", 16000, "ron", null, null);
            producto("Appleton Signature", 20000, "ron", null, null);
            producto("Appleton Reserva", 23000, "ron", null, null);

            // Tequila
            producto("Cuerna Vaca", 6000, "tequila", null, null);
            producto("Conquistador", 8000, "tequila", null, null);
            producto("José Cuervo Blanco", 13000, "tequila", null, null);
            producto("José Cuervo Dorado", 13000, "tequila", null, null);

            // Whiskies
            producto("JB", 10000, "whisky", null, null);
            producto("Red Label", 12000, "whisky", null, null);
            producto("Ballantine's Finest", 10000, "whisky", null, null);
            producto("Jim Beam", 12000, "whisky", null, null);
            producto("Jameson", 12000, "whisky", null, null);
            producto("Jack Daniels", 17000, "whisky", null, null);
            producto("Jack Daniels Honey", 17000, "whisky", null, null);
            producto("Black Label", 16000, "whisky", null, null);
            producto("Chivas", 18000, "whisky", null, null);
            producto("Buchanan's de Luxe", 18000, "whisky", null, null);
            producto("Swing", 39000, "whisky", null, null);
            producto("Blue Label", 84000, "whisky", null, null);
        }

        if (empleadoRepository.count() == 0) {
            empleadoRepository.save(
                    new Empleado("Camarero Barra", "1234")
            );
        }
    }

    private void producto(
            String nombre,
            double precio,
            String categoria,
            String descripcion,
            String imagenUrl
    ) {
        productoRepository.save(
                new Producto(
                        nombre,
                        descripcion,
                        precio,
                        50,
                        categoria,
                        imagenUrl,
                        true
                )
        );
    }
}