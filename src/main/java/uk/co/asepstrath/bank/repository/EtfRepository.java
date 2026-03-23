package uk.co.asepstrath.bank.repository;

import uk.co.asepstrath.bank.model.Constituent;
import uk.co.asepstrath.bank.model.Etf;
import uk.co.asepstrath.bank.model.dto.EtfDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtfRepository {

    private DataSource ds;
    private ConstituentRepository constituentRepository;

    public EtfRepository(DataSource ds) {
        this.ds = ds;
        this.constituentRepository = new ConstituentRepository(ds);
    }

    public void setUp() throws SQLException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS etf (
                            ticker VARCHAR(20) PRIMARY KEY,
                            name VARCHAR(100),
                            description VARCHAR (1000),
                            inception_date VARCHAR (15)
                        )
                    """);
        }
    }

    public void saveAll(List<Etf> etfs) throws SQLException {
        String sql = """
                MERGE INTO etf (ticker, name, description, inception_date)
                VALUES (?, ?, ?, ?)
                """;


        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (etfs != null) {
                for (Etf etf : etfs) {
                    ps.setString(1, etf.getTicker());
                    ps.setString(2, etf.getName());
                    ps.setString(3, etf.getDescription());
                    ps.setString(4, etf.getInceptionDate());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        }
    }

    public List<Etf> getAll() throws SQLException {
        EtfDTO etfDTO = new EtfDTO();
        String sql = """
                SELECT * FROM equity
                """;

        List<Etf> etfs = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();


            while (rs.next()) {
                etfs.add(new Etf(rs.getString("name"), rs.getString("ticker"), rs.getString("description"), rs.getString("inception_date"), null));
            }

            for (Etf etf : etfs) {
                List<Constituent> constituents = constituentRepository.getByEtf(etf.getTicker());
                etf.setConstituents(constituents);
            }

            return etfs;
        }
    }

    public Etf getEtf(String ticker) throws SQLException {
        String sql = """
                SELECT * FROM etf
                WHERE ticker = ?""";

        Etf etf = null;

        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, ticker);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                etf = new Etf(rs.getString("name"), rs.getString("ticker"), rs.getString("description"), rs.getString("inception_date"), null);
            }

            if (etf != null) {
                List<Constituent> constituents = constituentRepository.getByEtf(etf.getTicker());
                etf.setConstituents(constituents);
            }


            return etf;
        }
    }
}
