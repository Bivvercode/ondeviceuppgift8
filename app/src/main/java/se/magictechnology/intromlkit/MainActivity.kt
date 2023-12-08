package se.magictechnology.intromlkit

import android.graphics.BitmapFactory
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import se.magictechnology.intromlkit.ui.theme.IntroMLKitTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntroMLKitTheme {
                var imageText1 by remember {
                    mutableStateOf("")
                }
                var imageText2 by remember {
                    mutableStateOf("")
                }
                var imageText3 by remember {
                    mutableStateOf("")
                }
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.skylt1),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Button(onClick = {
                                imageText1 = ""
                                runTextRecognition(R.drawable.skylt1) { listOfWords ->
                                    for (word in listOfWords) {
                                        imageText1 += "\n${word}"
                                    }
                                }
                            }) {
                                Text("Process")
                            }
                            Text(
                                text = imageText1
                            )
                        }
                        Column {
                            Image(
                                painter = painterResource(R.drawable.skylt2),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Button(onClick = {
                                imageText2 = ""
                                runTextRecognition(R.drawable.skylt2) { listOfWords ->
                                    for (word in listOfWords) {
                                        imageText2 += "\n${word}"
                                    }
                                }
                            }) {
                                Text("Process")
                            }
                            Text(
                                text = imageText2
                            )
                        }
                        Column {
                            Image(
                                painter = painterResource(R.drawable.skylt3),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(100.dp)
                            )
                            Button(onClick = {
                                imageText3 = ""
                                runTextRecognition(R.drawable.skylt3) { listOfWords ->
                                    for (word in listOfWords) {
                                        imageText3 += "\n${word}"
                                    }
                                }
                            }) {
                                Text("Process")
                            }
                            Text(
                                text = imageText3
                            )
                        }
                    }
                }
            }
        }
    }

    private fun runTextRecognition(imageID: Int, callback: (List<String>) -> Unit) {
        var listOfWords = mutableListOf<String>()
        var selectedImage = BitmapFactory.decodeResource(resources, imageID)

        val image = InputImage.fromBitmap(selectedImage, 0)
        var textRecognizerOptions = TextRecognizerOptions.Builder().build()
        val recognizer = TextRecognition.getClient(textRecognizerOptions)
        //mTextButton.setEnabled(false)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                //mTextButton.setEnabled(true)
                listOfWords = processTextRecognitionResult(texts)
                callback.invoke(listOfWords)
            }
            .addOnFailureListener { e -> // Task failed with an exception
                //mTextButton.setEnabled(true)
                e.printStackTrace()
                callback.invoke(emptyList())
            }
    }

    private fun processTextRecognitionResult(texts: Text): MutableList<String> {
        var listOfWords = mutableListOf<String>()
        val blocks: List<Text.TextBlock> = texts.getTextBlocks()
        if (blocks.size == 0) {
            //showToast("No text found")
            return mutableListOf()
        }
        //mGraphicOverlay.clear()
        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].getLines()
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].getElements()
                for (k in elements.indices) {
                    //val textGraphic: Graphic = TextGraphic(mGraphicOverlay, elements[k])
                    //mGraphicOverlay.add(textGraphic)
                    listOfWords.add(elements[k].text)
                }
            }
        }
        return listOfWords
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )


    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntroMLKitTheme {
        Greeting("Android")
    }
}