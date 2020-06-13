package ayds.dodo.movieinfo.moredetails.view

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.moredetails.model.MoreDetailsModel
import ayds.dodo.movieinfo.moredetails.model.entities.TmdbMovie
import java.awt.Desktop
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.event.HyperlinkEvent


class moredetailsViewImp(moredetailsModel: MoreDetailsModel) : MoredetailsView {
    private val moredetails: MoreDetailsModel = moredetailsModel
    private var contentPane: JPanel = JPanel()
    private var movieDescriptionPane: JTextPane = JTextPane()
    private var imagePanel: JPanel = JPanel()
    private val FRAME_TITLE = "Movie Info Dodo"
    private val MORE_DETAILS_HEADER = "Data from The Movie Data Base"
    private val TEXT_TYPE = "text/html"

    override fun openView(movie: OmdbMovie) {
        initOtherInfoWindow()
        setupOtherInfoFrame()
    }

    private fun initOtherInfoWindow() {
        contentPane.layout = BoxLayout(contentPane, BoxLayout.PAGE_AXIS)
        val label = JLabel()
        label.text = MORE_DETAILS_HEADER
        contentPane.add(label)
        contentPane.add(imagePanel)
        val descriptionPanel: JPanel = setupMovieDescriptionPane()
        contentPane.add(descriptionPanel)
    }

    private fun setupOtherInfoFrame() {
        val frame = JFrame(FRAME_TITLE)
        frame.minimumSize = Dimension(600, 600)
        frame.contentPane = contentPane
        frame.pack()
        frame.isVisible = true
    }

    private fun setupMovieDescriptionPane(): JPanel {
        val descriptionPanel = JPanel()
        movieDescriptionPane = JTextPane()
        movieDescriptionPane.isEditable = false
        movieDescriptionPane.contentType = TEXT_TYPE
        movieDescriptionPane.maximumSize = Dimension(600, 400)
        descriptionPanel.add(movieDescriptionPane)
        return descriptionPanel
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
        val label = JLabel(ImageIcon(image))
        imagePanel.add(label)
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
                .getMovieDescriptionText(tmdbMovie);
    }

}