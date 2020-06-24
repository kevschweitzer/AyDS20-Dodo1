package ayds.dodo.movieinfo.moredetails.view

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

class MoreDetailsViewImp(private val moreDetailsModel: MoreDetailsModel) : MoreDetailsView {
    companion object {
        private const val FRAME_TITLE = "Movie Info Dodo"
        private const val MORE_DETAILS_HEADER = "Data from The Movie Data Base"
        private const val TEXT_TYPE = "text/html"
        private const val POSTER_BUTTON_TEXT = "View Movie Poster"
    }

    private val moreDetailsContentPane: JPanel = JPanel()
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

    private fun openView() {
        frame.isVisible = true
    }

    override fun onUiEvent(): Observable<MoreDetailsUiEvent> {
        return onActionSubject
    }

    private fun initObservers() {
        moreDetailsModel.movieObservable().subscribe(object : Observer<TmdbMovie> {
            override fun update(movie: TmdbMovie) {
                updateUI(movie)
                openView()
            }
        })
    }

    private fun initOtherInfoWindow() {
        headerLabel.text = MORE_DETAILS_HEADER
        setupMovieDescriptionPane()
        moreDetailsContentPane.apply {
            layout = BoxLayout(moreDetailsContentPane, BoxLayout.PAGE_AXIS)
            add(headerLabel)
            add(imagePanel)
            add(descriptionPanel)
            add(posterButton)
        }
    }

    private fun setupOtherInfoFrame() {
        frame.apply {
            minimumSize = Dimension(600, 600)
            contentPane = moreDetailsContentPane
            pack()
            isVisible = false
        }

    }

    private fun setupPosterButton() {
        posterButton.apply {
            text = POSTER_BUTTON_TEXT
            addActionListener { e: ActionEvent ->
                onActionSubject.notify(MoreDetailsUiEvent.POSTER_ACTION)
            }
        }
    }

    private fun setupMovieDescriptionPane() {
        movieDescriptionPane.apply {
            isEditable = false
            contentType = TEXT_TYPE
            maximumSize = Dimension(600, 400)
            descriptionPanel.add(this)
        }
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
        moreDetailsContentPane.validate()
        moreDetailsContentPane.repaint()
    }

    private fun populateDescriptionTextPane(tmdbMovie: TmdbMovie) {
        movieDescriptionPane.apply {
            contentType = TEXT_TYPE
            text = TmdbMovieDescriptionHelperImpl().getMovieDescriptionText(tmdbMovie)
        }
    }
}