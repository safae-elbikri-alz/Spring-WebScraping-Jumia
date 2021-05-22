import com.springapp.WebScrapping.dao.PrixDAO;
import com.springapp.WebScrapping.dao.ProduitDAO;
import com.springapp.WebScrapping.dao.SousCategorieDAO;
import com.springapp.WebScrapping.daoImpl.PrixDAOImpl;
import com.springapp.WebScrapping.daoImpl.ProduitDAOImpl;
import com.springapp.WebScrapping.daoImpl.SousCategorieDAOImpl;
import com.springapp.WebScrapping.jsoup.SousCategorieScraper;
import com.springapp.WebScrapping.models.Prix;
import com.springapp.WebScrapping.models.Produit;
import com.springapp.WebScrapping.models.SousCategorie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/scraping", "root", "fahd.arh@gmail.com");

        int id = 300;
        try {
            SousCategorieScraper scs = new SousCategorieScraper();
            ProduitDAO pd = new ProduitDAOImpl(conn);
            SousCategorieDAO scd = new SousCategorieDAOImpl(conn);
            PrixDAO prd = new PrixDAOImpl(conn);

            SousCategorie sousCategorie = scd.get(id);
            List<Produit> list = scs.scrapProduits(sousCategorie);
            for (Produit prod : list) {
                prod.setId_sous_categorie(id);
                int id_prod = pd.save(prod);
                if (id_prod != 0) {
                    Prix p = new Prix(prod.getPrix_produit(), prod.getDevise(), LocalTime.now(), LocalDate.now(), id_prod);
                    prd.save(p);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
