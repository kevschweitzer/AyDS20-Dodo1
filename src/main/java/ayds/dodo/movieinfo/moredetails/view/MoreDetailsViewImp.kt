package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.observer.Observable
import ayds.observer.Observer
import ayds.observer.Subject
import java.awt.Dimension
import java.awt.event.ActionEvent
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*

class MoreDetailsViewImp(moreDetailsModel: MoreDetailsModel) : MoreDetailsView {
    companion object {
        private const val FRAME_TITLE = "Movie Info Dodo"
        private const val MORE_DETAILS_HEADER = "Data from The Movie Data Base"
        private const val TEXT_TYPE = "text/html"
        private const val POSTER_BUTTON_TEXT = "View Movie Poster"
    }

    override lateinit var movie: OmdbMovie
    private val moreDetailsModel: MoreDetailsModel = moreDetailsModel
    private val contentPane: JPanel = JPanel()
    private val movieDescriptionPane: JTextPane = JTextPane()
    private val imagePanel: JPanel = JPanel()
    private val onActionSubject = Subject<MoreDetailsUiEvent>()
    private val descriptionPanel = JPanel()
    private val headerLabel = JLabel()
    private val imageLabel = JLabel()
    private val posterButton = JButton()
    private val frame = JFrame(FRAME_TITLE)

    init {
        initOtherInfoWindow()
        setupOtherInfoFrame()
        setupPosterButton()
        initObservers()
    }

    override fun openView(movie: OmdbMovie) {
        this.movie = movie
        onActionSubject.notify(MoreDetailsUiEvent.SEARCH_ACTION)
        frame.isVisible = true
    }

    override fun onUiEvent(): Observable<MoreDetailsUiEvent> {
        return onActionSubject
    }

    private fun initObservers() {
        moreDetailsModel.movieObservable().subscribe(object : Observer<TmdbMovie> {
            override fun update(movie: TmdbMovie) {
                updateUI(movie)
            }
        })
    }

    private fun initOtherInfoWindow() {
        contentPane.layout = BoxLayout(contentPane, BoxLayout.PAGE_AXIS)
        headerLabel.text = MORE_DETAILS_HEADER
        posterButton
        contentPane.add(headerLabel)
        contentPane.add(imagePanel)
        setupMovieDescriptionPane()
        contentPane.add(descriptionPanel)
        contentPane.add(posterButton)
    }

    private fun setupOtherInfoFrame() {
        frame.minimumSize = Dimension(600, 600)
        frame.contentPane = contentPane
        frame.pack()
        frame.isVisible = false
    }

    private fun setupPosterButton() {
        posterButton.text = POSTER_BUTTON_TEXT
        posterButton.addActionListener { e: ActionEvent ->
            onActionSubject.notify(MoreDetailsUiEvent.POSTER_ACTION)
        }
    }

    private fun setupMovieDescriptionPane() {
        movieDescriptionPane.isEditable = false
        movieDescriptionPane.contentType = TEXT_TYPE
        movieDescriptionPane.maximumSize = Dimension(600, 400)
        descriptionPanel.add(movieDescriptionPane)
    }

    fun updateUI(tmdbMovie: TmdbMovie) {
        val image: BufferedImage = getImageFromURL(tmdbMovie.imageUrl)
        setImageLabel(image)
        populateDescriptionTextPane(tmdbMovie)
    }

    private fun getImageFromURL(path: String): BufferedImage {
        val url = URL(path)
        return ImageIO.read(url)
    }

    private fun setImageLabel(image: BufferedImage) {
        imageLabel.icon = ImageIcon(image)
        imagePanel.add(imageLabel)
        contentPane.validate()
        contentPane.repaint()
    }

    private fun populateDescriptionTextPane(tmdbMovie: TmdbMovie) {
        movieDescriptionPane.contentType = TEXT_TYPE
        movieDescriptionPane.text = TmdbMovieDescriptionHelperImpl()
                .getMovieDescriptionText(tmdbMovie)
    }

}