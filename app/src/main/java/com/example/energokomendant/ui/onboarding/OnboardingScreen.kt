package com.example.energokomendant.ui.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(onDone: () -> Unit) {
    BackHandler { onDone() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "ЭнергоКомендант",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "Ты — комендант старого здания. Жильцы мёрзнут, счета за энергию растут, а бюджет тает.",
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        OnboardingCard(
            emoji = "🎯",
            title = "Твоя цель",
            text = "За 5 дней снизить расход энергии до целевого и не заморозить жильцов."
        )

        OnboardingCard(
            emoji = "📅",
            title = "Каждый день",
            text = "Сначала случайное событие с выбором. Потом магазин улучшений. Затем жми «Закончить день» — получишь доход и счёт за энергию."
        )

        OnboardingCard(
            emoji = "⚖️",
            title = "Три цифры",
            text = "Бюджет — деньги. Расход — сколько энергии тратит дом. Комфорт — довольны ли жильцы. Всё должно остаться в норме."
        )

        OnboardingCard(
            emoji = "🛒",
            title = "Покупай с умом",
            text = "Каждое улучшение снижает расход или меняет комфорт. Перед покупкой видишь прогноз: как изменится бюджет и цель."
        )

        OnboardingCard(
            emoji = "⚡",
            title = "Зарабатывай энергокоины",
            text = "Проходи уровни и мини-игры — получай ⚡. Трать на поставщиков и сотрудников в городе: они делают прохождение легче."
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text("💡 Почему это важно", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Каждый день мы тратим энергию, которую можно было сэкономить. Энергосбережение — это не отказ от удобств, а умное использование того, что у нас есть.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text("Начать", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun OnboardingCard(emoji: String, title: String, text: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(title, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text, fontSize = 12.sp, lineHeight = 16.sp)
            }
        }
    }
}