package ayds.dodo.movieinfo.moredetails.fulllogic;

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import static ayds.dodo.movieinfo.moredetails.fulllogic.NonExistentTmdbMovie.IMAGE_NOT_FOUND;

public class OtherInfoWindow {
    private JPanel contentPane;
    private JTextPane movieDescriptionPane;
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
    private static final String IMAGE_URL_BASE = "https://image.tmdb.org/t/p/w400/";
    private static final String RESULTS_JSON = "results";
    private static final String OVERVIEW_JSON = "overview";
    private static final String POSTER_PATH_JSON = "poster_path";
    private static final String BACKDROP_PATH_JSON = "backdrop_path";
    private static final String RELEASE_DATE = "release_date";

    public void getMoviePlot(OmdbMovie movie) {
        new Thread(() -> initOtherInfoData(movie)).start();
    }

    private void initOtherInfoData(OmdbMovie movie) {
        TmdbMovie tmdbMovieSearched = getTmdbMovie(movie);
        setSystemLookAndFeel();
        updateUI(tmdbMovieSearched);
    }

    private TmdbMovie getTmdbMovie(OmdbMovie movie) {
        TmdbMovie tmdbMovieSearched = DataBase.getTmdbMovie(movie.getTitle());

        if (tmdbMovieSearched == NonExistentTmdbMovie.INSTANCE) {
            tmdbMovieSearched = getTmdbMovieFromServer(movie);
            DataBase.saveMovieInfo(tmdbMovieSearched);
        }
        return tmdbMovieSearched;
    }

    private TmdbMovie getTmdbMovieFromServer(OmdbMovie movie) {
        try {
            Iterator<JsonElement> resultIterator = getJsonElementIterator( movie);
            JsonObject result = getInfoFromTmdb(resultIterator, movie);
            if (result != null) {
                String backdropPath = getBackdrop(result);
                JsonElement posterPath = result.get(POSTER_PATH_JSON);
                JsonElement extract = result.get(OVERVIEW_JSON);

                if (extract != JsonNull.INSTANCE && posterPath != JsonNull.INSTANCE) {
                    String path = backdropPath != null && !backdropPath.equals("") ? IMAGE_URL_BASE + backdropPath : IMAGE_NOT_FOUND;
                    String text = formatText(movie, posterPath, extract);
                    TmdbMovie tmdbMovieSearched = createTmdbMovie(movie, text, path, posterPath);
                    return tmdbMovieSearched;
                }
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return NonExistentTmdbMovie.INSTANCE;
    }

    @NotNull
    private TmdbMovie createTmdbMovie(OmdbMovie movie, String text, String path, JsonElement posterPath) {
        TmdbMovie tmdbMovie = new TmdbMovie();
        tmdbMovie.setTitle(movie.getTitle());
        tmdbMovie.setPlot(text);
        tmdbMovie.setImageUrl(path);
        tmdbMovie.setPosterUrl(posterPath.getAsString());
        return tmdbMovie;
    }

    private void updateUI(TmdbMovie tmdbMovieSearched) {
        try {
            BufferedImage image = getImageFromURL(tmdbMovieSearched.getImageUrl());
            setImageLabel(image);
            populateDescriptionTextPane(tmdbMovieSearched.getPlot());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private BufferedImage getImageFromURL(String path) throws IOException {
        URL url = new URL(path);
        return ImageIO.read(url);
    }

    private void populateDescriptionTextPane(String text) {
        movieDescriptionPane.setContentType(TEXT_TYPE);
        movieDescriptionPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        movieDescriptionPane.setText(text);
    }

    @NotNull
    private Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(TMDB_URL_BASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void open(OmdbMovie movie) {
        initOtherInfoWindow();
        setupOtherInfoFrame();
        DataBase.createNewDatabase();
        getMoviePlot(movie);
    }

    private void  initOtherInfoWindow() {
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        JLabel label = new JLabel();
        label.setText(MORE_DETAILS_HEADER);
        contentPane.add(label);

        imagePanel = new JPanel();
        contentPane.add(imagePanel);

        JPanel descriptionPanel = setupMovieDescriptionPane();
        contentPane.add(descriptionPanel);

    }

    private void setupOtherInfoFrame() {
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setMinimumSize(new Dimension(600, 600));
        frame.setContentPane(contentPane);
        frame.pack();
        frame.setVisible(true);
    }


    private JPanel setupMovieDescriptionPane() {
        JPanel descriptionPanel = new JPanel();
        movieDescriptionPane = new JTextPane();
        movieDescriptionPane.setEditable(false);
        movieDescriptionPane.setContentType(TEXT_TYPE);
        movieDescriptionPane.setMaximumSize(new Dimension(600, 400));
        descriptionPanel.add(movieDescriptionPane);
        return descriptionPanel;
    }

    public String textToHtml(String text, String term) {
        StringBuilder builder = new StringBuilder(HTML_OPEN + BODY_STYLE);
        builder.append(FONT_STYLE_OPEN);
        String textWithBold = text
                .replace("'", "`")
                .replaceAll("(?i)" + term, BOLD_OPEN + term.toUpperCase() + BOLD_CLOSE);
        builder.append(textWithBold);
        builder.append(FONT_CLOSE);
        return builder.toString();
    }

    private String createMoviePosterAnchor(JsonElement path) {
        return "<a href=" + IMAGE_URL_BASE + path.getAsString() + ">View Movie Poster</a>";
    }

    @NotNull
    private String formatText(OmdbMovie movie, JsonElement posterPath, JsonElement extract) {
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

    private void setImageLabel(BufferedImage image){
        JLabel label = new JLabel(new ImageIcon(image));
        imagePanel.add(label);
        contentPane.validate();
        contentPane.repaint();
    }

    @Nullable
    private JsonObject getInfoFromTmdb(Iterator<JsonElement> resultIterator, OmdbMovie movie){
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
    private Iterator<JsonElement> getJsonElementIterator(OmdbMovie movie) throws java.io.IOException {
        Retrofit retrofit = getRetrofit();
        TheMovieDBAPI tmdbAPI = retrofit.create(TheMovieDBAPI.class);

        Response<String> callResponse = tmdbAPI.getTerm(movie.getTitle()).execute();

        Gson gson = new Gson();
        JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);

        return jobj.get(RESULTS_JSON).getAsJsonArray().iterator();
    }
}

