package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AppLanguage
import com.example.model.UserProfile
import com.example.util.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    onUpdateProfile: (name: String, username: String, bio: String, statusMood: String, avatarUrl: String) -> Unit,
    isDark: Boolean,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    var showEditSheet by remember { mutableStateOf(false) }
    val textColor = if (isDark) Color.White else Color(0xFF17253D)
    val subTextColor = if (isDark) Color(0xFF94A3B8) else Color(0xFF6F8098)

    val sampleMemories = listOf(
        "https://images.unsplash.com/photo-1519681393784-d120267933ba?w=500&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=500&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=500&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=500&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=500&auto=format&fit=crop&q=80",
        "https://images.unsplash.com/photo-1518495973542-4542c06a5843?w=500&auto=format&fit=crop&q=80"
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Profile Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "@${userProfile.username}",
                        color = textColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = if (isDark) Color(0xFF83C9FF) else Color(0xFF65B9FF),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = { showEditSheet = true },
                    modifier = Modifier.testTag("edit_profile_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = textColor
                    )
                }
            }

            // Instagram-like enhanced main section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Large Avatar with Gradient Vibe Story Ring
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.sweepGradient(
                                listOf(
                                    Color(0xFF83C9FF),
                                    Color(0xFF818CF8),
                                    Color(0xFFE879F9),
                                    Color(0xFF83C9FF)
                                )
                            )
                        )
                        .padding(3.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(2.dp, if (isDark) Color(0xFF0B132B) else Color.White, CircleShape)
                    ) {
                        AsyncImage(
                            model = userProfile.avatarUrl,
                            contentDescription = userProfile.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // Stats: Vibes, Amigos, Hilos
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 24.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    ProfileStatItem("Vibes", "142", textColor, subTextColor)
                    ProfileStatItem("Amigos", "${userProfile.friendsCount}", textColor, subTextColor)
                    ProfileStatItem("Hilos", "${userProfile.threadsCount}", textColor, subTextColor)
                }
            }

            // Name, Bio, Mood Status
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 6.dp)
            ) {
                Text(
                    text = userProfile.name,
                    color = textColor,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Custom Status / Music Mood pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isDark) Color(0xFF1E293B) else Color(0xFFE0F2FE))
                        .border(1.dp, Color(0xFF83C9FF).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Headphones,
                        contentDescription = "Mood",
                        tint = if (isDark) Color(0xFF83C9FF) else Color(0xFF65B9FF),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = userProfile.statusMood,
                        color = if (isDark) Color.White else Color(0xFF0369A1),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = userProfile.bio,
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }

            // Action Buttons: Edit Profile, Share QR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { showEditSheet = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDark) Color(0xFF1E293B) else Color(0xFFDCEBFA)
                    )
                ) {
                    Text(
                        text = Strings.editProfile(language),
                        color = textColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { /* Share QR Code */ },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isDark) Color(0xFF1E293B) else Color(0xFFDCEBFA))
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        tint = textColor
                    )
                }
            }

            // Highlight Bubbles (Historias destacadas)
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(userProfile.highlights) { highlight ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(72.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(CircleShape)
                                .background(if (isDark) Color(0xFF1E293B) else Color(0xFFF1F5F9))
                                .border(1.5.dp, if (isDark) Color(0xFF83C9FF) else Color(0xFF65B9FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = highlight.takeLast(2),
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = highlight.dropLast(2).trim(),
                            color = textColor,
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Grid of shared memories/vibes
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(start = 2.dp, end = 2.dp, bottom = 110.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(sampleMemories) { photoUrl ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    ) {
                        AsyncImage(
                            model = photoUrl,
                            contentDescription = "Memory",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }

        // Edit Profile Sheet
        if (showEditSheet) {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            var editName by remember { mutableStateOf(userProfile.name) }
            var editUsername by remember { mutableStateOf(userProfile.username) }
            var editBio by remember { mutableStateOf(userProfile.bio) }
            var editMood by remember { mutableStateOf(userProfile.statusMood) }
            var editAvatar by remember { mutableStateOf(userProfile.avatarUrl) }

            ModalBottomSheet(
                onDismissRequest = { showEditSheet = false },
                sheetState = sheetState,
                containerColor = if (isDark) Color(0xFF0B132B) else Color(0xFFF5FAFF),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 36.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Strings.editProfile(language),
                            color = textColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { showEditSheet = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = subTextColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Nombre para mostrar") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF83C9FF)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editUsername,
                        onValueChange = { editUsername = it },
                        label = { Text("Nombre de usuario (@)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF83C9FF)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editMood,
                        onValueChange = { editMood = it },
                        label = { Text("Estado / Mood musical") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF83C9FF)
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = editBio,
                        onValueChange = { editBio = it },
                        label = { Text("Biografía") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF83C9FF)
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            onUpdateProfile(editName, editUsername, editBio, editMood, editAvatar)
                            showEditSheet = false
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isDark) Color(0xFF83C9FF) else Color(0xFF65B9FF)
                        )
                    ) {
                        Text(
                            text = Strings.save(language),
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileStatItem(label: String, count: String, textColor: Color, subTextColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            color = textColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = subTextColor,
            fontSize = 12.sp
        )
    }
}
