package ayds.dodo.movieinfo.moredetails.fulllogic;

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.event.HyperlinkEvent;

import java.net.URL;
import java.util.Iterator;
import java.io.IOException;

public class OtherInfoWindow {
    private JPanel contentPane;
    private JTextPane textPane2;
    private JPanel imagePanel;
    private final String TMDB_URL_BASE = "https://api.themoviedb.org/3/";
    private static final String FRAME_TITLE = "Movie Info Dodo";
    private static final String MORE_DETAILS_HEADER = "Data from The Movie Data Base";
    private static final String TEXT_TYPE = "text/html";
    private static final String HTML_OPEN = "<html>";
    private static final String BODY_STYLE = "<body style=\"width: 400px;\">";
    private static final String FONT_STYLE_OPEN = "<font face=\"arial\">";
    private static final String FONT_CLOSE = "</font>";
    private static final String BOLD_OPEN = "<b>";
    private static final String BOLD_CLOSE = "</b>";

    public void getMoviePlot(OmdbMovie movie) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TMDB_URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        TheMovieDBAPI tmdbAPI = retrofit.create(TheMovieDBAPI.class);

        textPane2.setContentType(TEXT_TYPE);

        textPane2.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        new Thread(new OtherWindowRunnable(movie, tmdbAPI)).start();
    }

    public static void open(OmdbMovie movie) {

        OtherInfoWindow win = new OtherInfoWindow();

        win.contentPane = new JPanel();
        win.contentPane.setLayout(new BoxLayout(win.contentPane, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel();
        label.setText(MORE_DETAILS_HEADER);
        win.contentPane.add(label);

        win.imagePanel = new JPanel();
        win.contentPane.add(win.imagePanel);

        JPanel descriptionPanel = new JPanel();
        win.textPane2 = new JTextPane();
        win.textPane2.setEditable(false);
        win.textPane2.setContentType(TEXT_TYPE);
        win.textPane2.setMaximumSize(new Dimension(600, 400));
        descriptionPanel.add(win.textPane2);
        win.contentPane.add(descriptionPanel);

        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setContentPane(win.contentPane);
        frame.pack();
        frame.setVisible(true);

        DataBase.createNewDatabase();

        win.getMoviePlot(movie);
    }

    public static String textToHtml(String text, String term) {
        StringBuilder builder = new StringBuilder(HTML_OPEN + BODY_STYLE);

        builder.append(FONT_STYLE_OPEN);

        String textWithBold = text
                .replace("'", "`")
                .replaceAll("(?i)" + term, BOLD_OPEN + term.toUpperCase() + BOLD_CLOSE);

        builder.append(textWithBold);

        builder.append(FONT_CLOSE);

        return builder.toString();
    }


    private class OtherWindowRunnable implements Runnable {
        private final OmdbMovie movie;
        private final TheMovieDBAPI tmdbAPI;
        private final String IMAGE_NOT_FOUND = "https://farm5.staticflickr.com/4363/36346283311_1dec5bb2c2.jpg";
        private final String IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w400/";
        private final String RESULTS_JSON = "results";
        private final String OVERVIEW_JSON = "overview";
        private final String POSTER_PATH_JSON = "poster_path";
        private final String BACKDROP_PATH_JSON = "backdrop_path";
        private final String NO_RESULTS_MESSAGES = "No Results";
        private final String RELEASE_DATE = "release_date";

        public OtherWindowRunnable(OmdbMovie movie, TheMovieDBAPI tmdbAPI) {
            this.movie = movie;
            this.tmdbAPI = tmdbAPI;
        }

        @Override
        public void run() {
            String text = DataBase.getOverview(movie.getTitle());

            String path = DataBase.getImageUrl(movie.getTitle());

            if (text == null || path == null) {
                try {
                    path = IMAGE_NOT_FOUND;
                    JsonObject result = getInfoFromTmdb();
                    String backdropPath = null;
                    JsonElement posterPath = JsonNull.INSTANCE;
                    JsonElement extract = JsonNull.INSTANCE;

                    if (result != null){
                        backdropPath = getBackdrop(result);
                        posterPath = result.get(POSTER_PATH_JSON);
                        extract = result.get(OVERVIEW_JSON);
                    }

                    if (extract == JsonNull.INSTANCE || posterPath == JsonNull.INSTANCE) {
                        text = NO_RESULTS_MESSAGES;
                    } else {
                        text = formatText(posterPath, extract);

                        if (backdropPath != null && !backdropPath.equals(""))
                            path = IMAGE_URL_BASE + backdropPath;

                        DataBase.saveMovieInfo(movie.getTitle(), text, path, posterPath.getAsString());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                setImageLabel(path);
            }  catch (Exception e1) {
                e1.printStackTrace();
            }

            textPane2.setText(text);
        }

        private String createMoviePosterAnchor(JsonElement path){
            return "<a href="+ IMAGE_URL_BASE + path.getAsString() + ">View Movie Poster</a>";
        }

        @NotNull
        private String formatText(JsonElement posterPath, JsonElement extract) {
            String text;
            text = extract.getAsString().replace("\\n", "\n");
            text = textToHtml(text, movie.getTitle());
            text += "\n" + createMoviePosterAnchor(posterPath);
            return text;
        }

        @Nullable
        private String getBackdrop(JsonObject result) {
            JsonElement backdropPathJson = result.get(BACKDROP_PATH_JSON);

            String backdropPath = null;

            if (!backdropPathJson.isJsonNull()) {
                backdropPath = backdropPathJson.getAsString();
            }
            return backdropPath;
        }

        private void setImageLabel(String path) throws IOException {
            JLabel label;
            URL url = new URL(path);
            BufferedImage image = ImageIO.read(url);
            label = new JLabel(new ImageIcon(image));
            imagePanel.add(label);

            contentPane.validate();
            contentPane.repaint();
        }

        @Nullable
        private JsonObject getInfoFromTmdb() throws java.io.IOException {
            Iterator<JsonElement> resultIterator = getJsonElementIterator();

            JsonObject result = null;

            while (resultIterator.hasNext()) {
                result = resultIterator.next().getAsJsonObject();
                String year = result.get(RELEASE_DATE).getAsString().split("-")[0];

                if (year.equals(movie.getYear()))
                    break;
            }
            return result;
        }

        @NotNull
        private Iterator<JsonElement> getJsonElementIterator() throws java.io.IOException {
            Response<String> callResponse = tmdbAPI.getTerm(movie.getTitle()).execute();

            Gson gson = new Gson();
            JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);

            return jobj.get(RESULTS_JSON).getAsJsonArray().iterator();
        }
    }
}