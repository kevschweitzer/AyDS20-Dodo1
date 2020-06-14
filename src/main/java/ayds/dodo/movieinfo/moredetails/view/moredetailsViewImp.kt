package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import ayds.observer.Observable
import ayds.observer.Observer
import ayds.observer.Subject
import java.awt.Desktop
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.HyperlinkEvent


class MoreDetailsViewImp(moreDetailsModel: MoreDetailsModel) : MoreDetailsView {
    companion object {
        private const val FRAME_TITLE = "Movie Info Dodo"
        private const val MORE_DETAILS_HEADER = "Data from The Movie Data Base"
        private const val TEXT_TYPE = "text/html"
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
    private val frame = JFrame(FRAME_TITLE)

    override fun openView(movie: OmdbMovie) {
        this.movie = movie
        initOtherInfoWindow()
        setupOtherInfoFrame()
        initObservers()
        onActionSubject.notify(MoreDetailsUiEvent.SEARCH_ACTION)
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
        contentPane.add(headerLabel)
        contentPane.add(imagePanel)
        setupMovieDescriptionPane()
        contentPane.add(descriptionPanel)
    }

    private fun setupOtherInfoFrame() {
        frame.minimumSize = Dimension(600, 600)
        frame.contentPane = contentPane
        frame.pack()
        frame.isVisible = true
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
        movieDescriptionPane.addHyperlinkListener { e: HyperlinkEvent ->
            if (HyperlinkEvent.EventType.ACTIVATED == e.eventType) {
                val desktop = Desktop.getDesktop()
                try {
                    desktop.browse(e.url.toURI())
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        movieDescriptionPane.text = TmdbMovieDescriptionHelperImpl()
                .getMovieDescriptionText(tmdbMovie)
    }

}