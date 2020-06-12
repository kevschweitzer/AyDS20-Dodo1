package ayds.dodo.movieinfo.moredetails.view

import java.awt.Dimension
import javax.swing.*

class moredetailsViewImp(): MoredetailsView {
//class moredetailsViewImp(moredetailsModel:MoredetailsModel): MoredetailsView {
    //private val moredetails:MoredetailsModel
    private var contentPane: JPanel? = null
    private var movieDescriptionPane: JTextPane? = null
    private var imagePanel: JPanel? = null
    private val FRAME_TITLE = "Movie Info Dodo"
    private val MORE_DETAILS_HEADER = "Data from The Movie Data Base"
    private val TEXT_TYPE = "text/html"

    init{
        //this.moredetails = moredetailsModel
    }

    override fun openView() {
        initOtherInfoWindow()
        setupOtherInfoFrame()
    }

    private fun initOtherInfoWindow() {
        contentPane = JPanel()
        contentPane!!.setLayout(BoxLayout(contentPane, BoxLayout.PAGE_AXIS))
        val label = JLabel()
        label.text = MORE_DETAILS_HEADER
        contentPane!!.add(label)
        imagePanel = JPanel()
        contentPane!!.add(imagePanel)
        val descriptionPanel: JPanel = setupMovieDescriptionPane()
        contentPane!!.add(descriptionPanel)
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
        movieDescriptionPane!!.setEditable(false)
        movieDescriptionPane!!.setContentType(TEXT_TYPE)
        movieDescriptionPane!!.setMaximumSize(Dimension(600, 400))
        descriptionPanel.add(movieDescriptionPane)
        return descriptionPanel
    }

}