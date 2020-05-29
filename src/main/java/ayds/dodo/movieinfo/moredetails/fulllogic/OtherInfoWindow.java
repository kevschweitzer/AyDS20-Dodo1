package ayds.dodo.movieinfo.moredetails.fulllogic;

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Iterator;

public class OtherInfoWindow {
    private JPanel contentPane;
    private JTextPane textPane2;
    private JPanel imagePanel;

    public void getMoviePlot(OmdbMovie movie) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TheMovieDBAPI tmdbAPI = retrofit.create(TheMovieDBAPI.class);

        textPane2.setContentType("text/html");

        textPane2.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                System.out.println(e.getURL());
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String text = DataBase.getOverview(movie.getTitle());

                String path = DataBase.getImageUrl(movie.getTitle());

                if (text != null && path != null) {
                    text = "[*]" + text;
                } else {
                    Response<String> callResponse;
                    try {
                        callResponse = tmdbAPI.getTerm(movie.getTitle()).execute();

                        Gson gson = new Gson();
                        JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);

                        Iterator<JsonElement> resultIterator = jobj.get("results").getAsJsonArray().iterator();

                        JsonObject result = null;

                        while (resultIterator.hasNext()) {
                            result = resultIterator.next().getAsJsonObject();

                            String year = result.get("release_date").getAsString().split("-")[0];

                            if (year.equals(movie.getYear()))
                                break;
                        }

                        JsonElement extract = result.get("overview");

                        JsonElement backdropPathJson = result.get("backdrop_path");

                        String backdropPath = null;

                        if (!backdropPathJson.isJsonNull()) {
                            backdropPath = backdropPathJson.getAsString();
                        }

                        JsonElement posterPath = result.get("poster_path");

                        if (extract == null || posterPath == JsonNull.INSTANCE) {
                            text = "No Results";
                        } else {
                            text = extract.getAsString().replace("\\n", "\n")
                                    .replace("'", "`");

                            if (backdropPath != null)
                                path = "https://image.tmdb.org/t/p/w400/" + backdropPath;

                            DataBase.saveMovieInfo(movie.getTitle(), text, path);
                            text = textToHtml(text, movie.getTitle());

                            text += "\n" + "<a href=https://image.tmdb.org/t/p/w400/" + posterPath.getAsString() + ">View Movie Poster</a>";
                        }


                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }

                textPane2.setText(text);

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                try {
                    JLabel label;
                    if (path != null) {
                        URL url = new URL(path);
                        BufferedImage image = ImageIO.read(url);
                        label= new JLabel(new ImageIcon(image));
                    } else {
                        label = new JLabel("Image not found");
                    }
                    imagePanel.add(label);

                    contentPane.validate();
                    contentPane.repaint();

                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        }).start();
    }

    public static void open(OmdbMovie movie) {

        OtherInfoWindow win = new OtherInfoWindow();

        win.contentPane = new JPanel();
        win.contentPane.setLayout(new BoxLayout(win.contentPane, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel();
        label.setText("Data from The Movie Data Base");
        win.contentPane.add(label);

        win.imagePanel = new JPanel();
        win.contentPane.add(win.imagePanel);

        JPanel descriptionPanel = new JPanel();
        win.textPane2 = new JTextPane();
        win.textPane2.setEditable(false);
        win.textPane2.setContentType("text/html");
        win.textPane2.setMaximumSize(new Dimension(600, 400));
        descriptionPanel.add(win.textPane2);
        win.contentPane.add(descriptionPanel);

        JFrame frame = new JFrame("Movie Info Dodo");
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setContentPane(win.contentPane);
        frame.pack();
        frame.setVisible(true);

        DataBase.createNewDatabase();

        win.getMoviePlot(movie);
    }

    public static String textToHtml(String text, String term) {
        StringBuilder builder = new StringBuilder("<html><body style=\"width: 400px;\">");

        builder.append("<font face=\"arial\">");

        String textWithBold = text
                .replace("'", "`")
                .replaceAll("(?i)" + term, "<b>" + term.toUpperCase() + "</b>");

        builder.append(textWithBold);

        builder.append("</font>");

        return builder.toString();
    }

}