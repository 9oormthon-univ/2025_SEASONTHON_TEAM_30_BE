package backend.mydays.config.data;

import backend.mydays.domain.*;
import backend.mydays.domain.Character;
import backend.mydays.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ChallengeRepository challengeRepository;
	private final PostRepository postRepository;
	private final CommentRepository commentRepository;
	private final LikeRepository likeRepository;
	private final TitleRepository titleRepository;
	private final UserTitleRepository userTitleRepository;
	private final UserChallengeRepository userChallengeRepository;
	private final CharacterRepository characterRepository;

	@Override
	public void run(String... args) throws Exception {
		// 0. Create Characters
		Character character1 = createCharacterIfNotFound("새싹", 1, "/images/big/0.png", null);
		Character character2 = createCharacterIfNotFound("새싹", 2, "/images/big/1.png", null);
		Character character3 = createCharacterIfNotFound("불꽃", 3, "/images/big/2.png", 1);
		Character character4 = createCharacterIfNotFound("물방울", 3, "/images/big/3.png", 2);
		Character character5 = createCharacterIfNotFound("풀잎", 3, "/images/big/4.png", 3);
		Character character6 = createCharacterIfNotFound("불꽃", 4, "/images/big/5.png", 1);
		Character character7 = createCharacterIfNotFound("물방울", 4, "/images/big/6.png", 2);
		Character character8 = createCharacterIfNotFound("풀잎", 4, "/images/big/7.png", 3);
		Character character9 = createCharacterIfNotFound("불꽃", 5, "/images/big/8.png", 1);
		Character character10 = createCharacterIfNotFound("물방울", 5, "/images/big/9.png", 2);
		Character character11 = createCharacterIfNotFound("풀잎", 5, "/images/big/10.png", 3);

		// 1. Create Users
		Users user1 = createUserIfNotFound("test@test.com", "테스트유저", "password");
		Users user2 = createUserIfNotFound("user2@example.com", "챌린지마스터", "password");
		Users user3 = createUserIfNotFound("user3@example.com", "꾸준함의아이콘", "password");

		// 2. Create Challenges
		// Create 30-day Challenges
		Challenge challenge1 = createChallengeIfNotFound(LocalDate.of(2025, 8, 9),
			"당신을 행복하게 하는 음악을 찾아 듣고 공유해보세요.");
		Challenge challenge2 = createChallengeIfNotFound(LocalDate.of(2025, 8, 10),
			"오늘 하늘을 올려다보고 가장 예쁜 순간을 사진으로 담아보세요.");
		Challenge challenge3 = createChallengeIfNotFound(LocalDate.of(2025, 8, 11),
			"하루를 마무리하며 짧은 일기를 적어보세요.");
		Challenge challenge4 = createChallengeIfNotFound(LocalDate.of(2025, 8, 12),
			"오늘의 소소한 지름신을 기록해보세요(예시: 맛있는 커피, 귀여운 펜 등).");
		Challenge challenge5 = createChallengeIfNotFound(LocalDate.of(2025, 8, 13),
			"지금까지 읽은 책 중 가장 재미있었던 책을 소개해보세요.");
		Challenge challenge6 = createChallengeIfNotFound(LocalDate.of(2025, 8, 14),
			"나의 장점 세 가지를 적어 스스로를 응원해보세요.");
		Challenge challenge7 = createChallengeIfNotFound(LocalDate.of(2025, 8, 15),
			"좋아하는 카페나 음식점의 메뉴판을 사진으로 남겨보세요.");
		Challenge challenge8 = createChallengeIfNotFound(LocalDate.of(2025, 8, 16),
			"오늘 느낀 감정을 색깔 하나로 표현해보세요.");
		Challenge challenge9 = createChallengeIfNotFound(LocalDate.of(2025, 8, 17),
			"주변 풍경을 그림이나 스케치로 남겨보세요.");
		Challenge challenge10 = createChallengeIfNotFound(LocalDate.of(2025, 8, 18),
			"나만 아는 비밀 장소를 사진으로 남겨보세요.");
		Challenge challenge11 = createChallengeIfNotFound(LocalDate.of(2025, 8, 19),
			"오늘 나를 웃게 만든 순간 한 가지를 기록해보세요.");
		Challenge challenge12 = createChallengeIfNotFound(LocalDate.of(2025, 8, 20),
			"가장 자주 만지는 물건을 사진으로 남겨보세요.");
		Challenge challenge13 = createChallengeIfNotFound(LocalDate.of(2025, 8, 21),
			"오늘 하루를 상징하는 이모티콘 3개를 기록해보세요.");
		Challenge challenge14 = createChallengeIfNotFound(LocalDate.of(2025, 8, 22),
			"오늘 머문 공간 중 가장 마음에 드는 곳을 기록해보세요.");
		Challenge challenge15 = createChallengeIfNotFound(LocalDate.of(2025, 8, 23),
			"5분 산책하며 야외 사진을 찍어보세요.");
		Challenge challenge16 = createChallengeIfNotFound(LocalDate.of(2025, 8, 24),
			"좋아하는 명언 1개를 작성해보세요.");
		Challenge challenge17 = createChallengeIfNotFound(LocalDate.of(2025, 8, 25),
			"오늘 나에게 친절했던 사람을 기록하고 감사함을 전해보세요.");
		Challenge challenge18 = createChallengeIfNotFound(LocalDate.of(2025, 8, 26),
			"가방 안에 있는 물건 중 가장 좋아하는 것을 소개해주세요.");
		Challenge challenge19 = createChallengeIfNotFound(LocalDate.of(2025, 8, 27),
			"오늘의 나에게 짧은 응원 메시지를 적어보세요.");
		Challenge challenge20 = createChallengeIfNotFound(LocalDate.of(2025, 8, 28),
			"요즘 보고 있는 넷플릭스, 왓챠 시리즈물을 소개해보세요.");
		Challenge challenge21 = createChallengeIfNotFound(LocalDate.of(2025, 8, 29),
			"오늘 들었던 가장 기분 좋은 소리를 기록해보세요.");
		Challenge challenge22 = createChallengeIfNotFound(LocalDate.of(2025, 8, 30),
			"내 최애 옷 브랜드나 쇼핑몰을 소개해보세요.");
		Challenge challenge23 = createChallengeIfNotFound(LocalDate.of(2025, 8, 31),
			"인생 맛집을 한 곳 소개해보세요.");
		Challenge challenge24 = createChallengeIfNotFound(LocalDate.of(2025, 9, 1),
			"오늘의 퇴근/하교길 BGM을 공유해보세요.");
		Challenge challenge25 = createChallengeIfNotFound(LocalDate.of(2025, 9, 2),
			"당신에게 특별한 의미가 있는 물건 한 가지를 소개하고 그 이유를 설명해보세요.");
		Challenge challenge26 = createChallengeIfNotFound(LocalDate.of(2025, 9, 3),
			"오늘 맛본 음식 중 가장 맛있었던 것을 묘사해보세요.");
		Challenge challenge27 = createChallengeIfNotFound(LocalDate.of(2025, 9, 4),
			"오늘 만난 소소한 행복을 글로 남겨보세요.");
		Challenge challenge28 = createChallengeIfNotFound(LocalDate.of(2025, 9, 5),
			"오늘 나를 편안하게 만든 행동 한 가지를 기록해보세요.");
		Challenge challenge29 = createChallengeIfNotFound(LocalDate.of(2025, 9, 6),
			"오늘 누군가에게 해준 작은 친절을 기록하거나, 받은 친절을 기록해보세요.");
		Challenge challenge30 = createChallengeIfNotFound(LocalDate.of(2025, 9, 7),
			"오늘 시도한 새로운 경험이나 도전을 기록해보세요.");

		// 3. Create Posts (mock data with createdAt = challenge date)
		Post post1 = createPostIfNotFound(user1, challenge1, "오늘은 음악으로 하루를 시작했어요. 기분 최고!",
			"https://picsum.photos/id/101/400/300");
		post1.setCreatedAt(challenge1.getChallengeDate().atStartOfDay());

		Post post2 = createPostIfNotFound(user2, challenge2, "하늘이 너무 예뻐서 사진으로 남겼습니다.",
			"https://picsum.photos/id/102/400/300");
		post2.setCreatedAt(challenge2.getChallengeDate().atStartOfDay());

		Post post3 = createPostIfNotFound(user3, challenge3, "짧게 일기를 쓰니 마음이 정리되네요.",
			"https://picsum.photos/id/103/400/300");
		post3.setCreatedAt(challenge3.getChallengeDate().atStartOfDay());

		Post post4 = createPostIfNotFound(user1, challenge4, "오늘은 커피 한 잔으로 행복했습니다 ☕",
			"https://picsum.photos/id/104/400/300");
		post4.setCreatedAt(challenge4.getChallengeDate().atStartOfDay());

		Post post5 = createPostIfNotFound(user2, challenge5, "최근에 읽은 책을 소개합니다. 정말 재미있었어요!",
			"https://picsum.photos/id/105/400/300");
		post5.setCreatedAt(challenge5.getChallengeDate().atStartOfDay());

		Post post6 = createPostIfNotFound(user3, challenge6, "나의 장점 세 가지를 적으니 자신감이 생깁니다.",
			"https://picsum.photos/id/106/400/300");
		post6.setCreatedAt(challenge6.getChallengeDate().atStartOfDay());

		Post post7 = createPostIfNotFound(user1, challenge7, "좋아하는 카페 메뉴판을 남겼습니다.",
			"https://picsum.photos/id/107/400/300");
		post7.setCreatedAt(challenge7.getChallengeDate().atStartOfDay());

		Post post8 = createPostIfNotFound(user2, challenge8, "오늘의 감정은 파란색이네요. 차분합니다.",
			"https://picsum.photos/id/108/400/300");
		post8.setCreatedAt(challenge8.getChallengeDate().atStartOfDay());

		Post post9 = createPostIfNotFound(user3, challenge9, "풍경을 그림으로 표현해봤습니다.",
			"https://picsum.photos/id/109/400/300");
		post9.setCreatedAt(challenge9.getChallengeDate().atStartOfDay());

		Post post10 = createPostIfNotFound(user1, challenge10, "나만 아는 장소를 공유합니다 🌿",
			"https://picsum.photos/id/110/400/300");
		post10.setCreatedAt(challenge10.getChallengeDate().atStartOfDay());

		Post post11 = createPostIfNotFound(user2, challenge11, "오늘 웃게 만든 순간을 기록합니다.",
			"https://picsum.photos/id/111/400/300");
		post11.setCreatedAt(challenge11.getChallengeDate().atStartOfDay());

		Post post12 = createPostIfNotFound(user3, challenge12, "가장 자주 만지는 물건을 사진으로 남겼어요.",
			"https://picsum.photos/id/112/400/300");
		post12.setCreatedAt(challenge12.getChallengeDate().atStartOfDay());

		Post post13 = createPostIfNotFound(user1, challenge13, "오늘 하루는 😊🌞🎶 이렇게 표현할 수 있네요.",
			"https://picsum.photos/id/113/400/300");
		post13.setCreatedAt(challenge13.getChallengeDate().atStartOfDay());

		Post post14 = createPostIfNotFound(user2, challenge14, "머문 공간 중 가장 마음에 드는 곳을 기록했습니다.",
			"https://picsum.photos/id/114/400/300");
		post14.setCreatedAt(challenge14.getChallengeDate().atStartOfDay());

		Post post15 = createPostIfNotFound(user3, challenge15, "5분 산책하며 사진을 찍었습니다.",
			"https://picsum.photos/id/115/400/300");
		post15.setCreatedAt(challenge15.getChallengeDate().atStartOfDay());

		Post post16 = createPostIfNotFound(user1, challenge16, "좋아하는 명언을 공유합니다.",
			"https://picsum.photos/id/116/400/300");
		post16.setCreatedAt(challenge16.getChallengeDate().atStartOfDay());

		Post post17 = createPostIfNotFound(user2, challenge17, "오늘 친절했던 사람에게 감사 인사를 전합니다.",
			"https://picsum.photos/id/117/400/300");
		post17.setCreatedAt(challenge17.getChallengeDate().atStartOfDay());

		Post post18 = createPostIfNotFound(user3, challenge18, "가방 속에서 가장 좋아하는 물건을 소개합니다.",
			"https://picsum.photos/id/118/400/300");
		post18.setCreatedAt(challenge18.getChallengeDate().atStartOfDay());

		Post post19 = createPostIfNotFound(user1, challenge19, "오늘의 나에게 응원 메시지를 남깁니다!",
			"https://picsum.photos/id/119/400/300");
		post19.setCreatedAt(challenge19.getChallengeDate().atStartOfDay());

		Post post20 = createPostIfNotFound(user2, challenge20, "요즘 보는 시리즈물을 소개합니다 📺",
			"https://picsum.photos/id/120/400/300");
		post20.setCreatedAt(challenge20.getChallengeDate().atStartOfDay());

		Post post21 = createPostIfNotFound(user3, challenge21, "오늘 가장 기분 좋은 소리를 기록합니다.",
			"https://picsum.photos/id/121/400/300");
		post21.setCreatedAt(challenge21.getChallengeDate().atStartOfDay());

		Post post22 = createPostIfNotFound(user1, challenge22, "내 최애 브랜드를 공유합니다.",
			"https://picsum.photos/id/122/400/300");
		post22.setCreatedAt(challenge22.getChallengeDate().atStartOfDay());

		Post post23 = createPostIfNotFound(user2, challenge23, "인생 맛집을 소개해봅니다.",
			"https://picsum.photos/id/123/400/300");
		post23.setCreatedAt(challenge23.getChallengeDate().atStartOfDay());

		Post post24 = createPostIfNotFound(user3, challenge24, "퇴근길에 들은 음악이 너무 좋았어요.",
			"https://picsum.photos/id/124/400/300");
		post24.setCreatedAt(challenge24.getChallengeDate().atStartOfDay());

		Post post25 = createPostIfNotFound(user1, challenge25, "특별한 의미가 있는 물건을 공유합니다.",
			"https://picsum.photos/id/125/400/300");
		post25.setCreatedAt(challenge25.getChallengeDate().atStartOfDay());

		Post post26 = createPostIfNotFound(user2, challenge26, "오늘 맛본 음식 중 최고는 이것!",
			"https://picsum.photos/id/126/400/300");
		post26.setCreatedAt(challenge26.getChallengeDate().atStartOfDay());

		Post post27 = createPostIfNotFound(user3, challenge27, "소소한 행복을 글로 남깁니다.",
			"https://picsum.photos/id/127/400/300");
		post27.setCreatedAt(challenge27.getChallengeDate().atStartOfDay());

		Post post28 = createPostIfNotFound(user1, challenge28, "오늘 나를 편안하게 만든 행동은…",
			"https://picsum.photos/id/128/400/300");
		post28.setCreatedAt(challenge28.getChallengeDate().atStartOfDay());

		Post post29 = createPostIfNotFound(user2, challenge29, "작은 친절을 베풀고 받은 하루였습니다.",
			"https://picsum.photos/id/129/400/300");
		post29.setCreatedAt(challenge29.getChallengeDate().atStartOfDay());





		// 4. Create UserChallenges (link posts to challenges for user completion)
		createUserChallengeIfNotFound(user1, challenge1, post1);
		createUserChallengeIfNotFound(user2, challenge2, post2);
		createUserChallengeIfNotFound(user3, challenge3, post3);
		createUserChallengeIfNotFound(user1, challenge4, post4);
		createUserChallengeIfNotFound(user2, challenge5, post5);
		createUserChallengeIfNotFound(user3, challenge6, post6);
		createUserChallengeIfNotFound(user1, challenge7, post7);
		createUserChallengeIfNotFound(user2, challenge8, post8);
		createUserChallengeIfNotFound(user3, challenge9, post9);
		createUserChallengeIfNotFound(user1, challenge10, post10);
		createUserChallengeIfNotFound(user2, challenge11, post11);
		createUserChallengeIfNotFound(user3, challenge12, post12);
		createUserChallengeIfNotFound(user1, challenge13, post13);
		createUserChallengeIfNotFound(user2, challenge14, post14);
		createUserChallengeIfNotFound(user3, challenge15, post15);
		createUserChallengeIfNotFound(user1, challenge16, post16);
		createUserChallengeIfNotFound(user2, challenge17, post17);
		createUserChallengeIfNotFound(user3, challenge18, post18);
		createUserChallengeIfNotFound(user1, challenge19, post19);
		createUserChallengeIfNotFound(user2, challenge20, post20);
		createUserChallengeIfNotFound(user3, challenge21, post21);
		createUserChallengeIfNotFound(user1, challenge22, post22);
		createUserChallengeIfNotFound(user2, challenge23, post23);
		createUserChallengeIfNotFound(user3, challenge24, post24);
		createUserChallengeIfNotFound(user1, challenge25, post25);
		createUserChallengeIfNotFound(user2, challenge26, post26);
		createUserChallengeIfNotFound(user3, challenge27, post27);
		createUserChallengeIfNotFound(user1, challenge28, post28);
		createUserChallengeIfNotFound(user2, challenge29, post29);


		// 5. Create Likes
		createLikeIfNotFound(user2, post1);
		createLikeIfNotFound(user3, post1);
		createLikeIfNotFound(user1, post2);
		createLikeIfNotFound(user3, post2);
		createLikeIfNotFound(user2, post3);
		createLikeIfNotFound(user1, post4);
		createLikeIfNotFound(user3, post5);
		createLikeIfNotFound(user2, post6);
		createLikeIfNotFound(user1, post7);
		createLikeIfNotFound(user3, post8);
		createLikeIfNotFound(user2, post9);
		createLikeIfNotFound(user1, post10);
		createLikeIfNotFound(user3, post11);
		createLikeIfNotFound(user2, post12);
		createLikeIfNotFound(user1, post13);
		createLikeIfNotFound(user3, post14);
		createLikeIfNotFound(user2, post15);
		createLikeIfNotFound(user1, post16);
		createLikeIfNotFound(user3, post17);
		createLikeIfNotFound(user2, post18);
		createLikeIfNotFound(user1, post19);
		createLikeIfNotFound(user3, post20);
		createLikeIfNotFound(user2, post21);
		createLikeIfNotFound(user1, post22);
		createLikeIfNotFound(user3, post23);
		createLikeIfNotFound(user2, post24);
		createLikeIfNotFound(user1, post25);
		createLikeIfNotFound(user3, post26);
		createLikeIfNotFound(user2, post27);
		createLikeIfNotFound(user1, post28);
		createLikeIfNotFound(user3, post29);


		// 6. Create Comments
		createCommentIfNotFound(post1, user2, "정말 대단하세요!");
		createCommentIfNotFound(post1, user3, "저도 오늘부터 시작해봐야겠어요.");
		createCommentIfNotFound(post2, user1, "피부 좋아지셨다니 저도 꾸준히 해봐야겠네요!");
		createCommentIfNotFound(post3, user3, "명상 저도 해보고 싶어요.");
		createCommentIfNotFound(post4, user2, "커피 한 잔으로 행복하다니 공감돼요!");
		createCommentIfNotFound(post5, user1, "감사일기 좋은 습관이죠!");
		createCommentIfNotFound(post6, user3, "자신감을 가지게 되는 느낌이네요.");
		createCommentIfNotFound(post7, user2, "카페 메뉴판 공유 감사합니다!");
		createCommentIfNotFound(post8, user1, "색깔로 감정을 표현하다니 멋지네요.");
		createCommentIfNotFound(post9, user3, "그림으로 풍경을 담다니 감성적이에요.");
		createCommentIfNotFound(post10, user2, "비밀 장소라니 궁금하네요!");
		createCommentIfNotFound(post11, user1, "웃게 만든 순간이라니 보기 좋아요.");
		createCommentIfNotFound(post12, user3, "자주 만지는 물건이라 공감됩니다.");
		createCommentIfNotFound(post13, user2, "이모티콘으로 하루 표현 멋지네요!");
		createCommentIfNotFound(post14, user1, "마음에 드는 공간, 저도 가보고 싶네요.");
		createCommentIfNotFound(post15, user3, "산책하며 찍은 사진 너무 좋습니다.");
		createCommentIfNotFound(post16, user2, "명언 공유 감사합니다.");
		createCommentIfNotFound(post17, user1, "감사한 마음을 전하는 모습 멋져요.");
		createCommentIfNotFound(post18, user3, "좋아하는 물건 소개 재미있네요.");
		createCommentIfNotFound(post19, user2, "자신에게 응원 메시지라니 감동적이에요.");
		createCommentIfNotFound(post20, user1, "시리즈물 소개 감사합니다!");
		createCommentIfNotFound(post21, user3, "기분 좋은 소리 공유 좋네요.");
		createCommentIfNotFound(post22, user2, "브랜드 소개 멋집니다.");
		createCommentIfNotFound(post23, user1, "인생 맛집 소개 감사합니다!");
		createCommentIfNotFound(post24, user3, "퇴근길 BGM 좋네요!");
		createCommentIfNotFound(post25, user2, "특별한 의미 있는 물건, 흥미롭네요.");
		createCommentIfNotFound(post26, user1, "맛있는 음식 묘사 잘했어요!");
		createCommentIfNotFound(post27, user3, "소소한 행복 공유 너무 좋아요.");
		createCommentIfNotFound(post28, user2, "편안하게 만든 행동, 공감합니다.");
		createCommentIfNotFound(post29, user1, "작은 친절 기록 좋네요.");


		// 7. Create Titles
		Title title1 = createTitleIfNotFound("스타터 🏁", "하루 챌린지를 처음 시작한 사용자", "#FFFFFF");
		Title title2 = createTitleIfNotFound("소확행러 🫰", "누적 미션 10개 넘는 사용자", "#FEBC2F");
		Title title3 = createTitleIfNotFound("챌린저 🚴", "미션을 한달이상 수행한 사용자", "#A35CFF");
		Title title4 = createTitleIfNotFound("하트부자 💗", "누적 하트 수 100개를 받은 사용자", "#FF83DA");
		Title title5 = createTitleIfNotFound("소통왕 🗽", "댓글 50개 이상 단 사용자", "#0FF2FF");


		// 8. Assign User Titles
		createUserTitleIfNotFound(user1, title1);
		createUserTitleIfNotFound(user2, title2);
		createUserTitleIfNotFound(user3, title3);
		createUserTitleIfNotFound(user1, title4);
		createUserTitleIfNotFound(user1, title3);

		// Set active titles for users
		user1.updateActiveTitle(title1);
		user1.setCharacter(character1);
		userRepository.save(user1);
		user2.updateActiveTitle(title2);
		user2.setCharacter(character2);
		userRepository.save(user2);
		user3.updateActiveTitle(title3);
		user3.setCharacter(character3);
		userRepository.save(user3);
	}

	private Users createUserIfNotFound(String email, String nickname, String password) {
		return userRepository.findByEmail(email).orElseGet(() -> {
			Users newUser = Users.builder()
				.nickname(nickname)
				.email(email)
				.password(passwordEncoder.encode(password))
				.build();
			Character defaultCharacter = characterRepository.findByLevel(1)
				.orElseThrow(() -> new RuntimeException("Level 1 character not found!"));
			newUser.setCharacter(defaultCharacter);
			return userRepository.save(newUser);
		});
	}

	private Challenge createChallengeIfNotFound(LocalDate date, String content) {
		return challengeRepository.findByChallengeDate(date).orElseGet(() -> {
			Challenge newChallenge = Challenge.builder()
				.challengeDate(date)
				.content(content)
				.build();
			return challengeRepository.save(newChallenge);
		});
	}

	private Post createPostIfNotFound(Users user, Challenge challenge, String content,
		String imageUrl) {
		// Check if a post already exists for this user and challenge
		return postRepository.findByUserAndChallenge(user, challenge).orElseGet(() -> {
			Post newPost = Post.builder()
				.user(user)
				.challenge(challenge)
				.content(content)
				.imageUrl(imageUrl)
				.build();
			newPost.setCreatedAt(challenge.getChallengeDate().atStartOfDay());
			return postRepository.save(newPost);
		});
	}

	private Comment createCommentIfNotFound(Post post, Users user, String content) {
		// Simple check to avoid duplicate comments with same content by same user on same post
		return commentRepository.findByPostAndUserAndContent(post, user, content).orElseGet(() -> {
			Comment newComment = Comment.builder()
				.post(post)
				.user(user)
				.content(content)
				.build();
			return commentRepository.save(newComment);
		});
	}

	private Like createLikeIfNotFound(Users user, Post post) {
		return likeRepository.findByUserAndPost(user, post).orElseGet(() -> {
			Like newLike = Like.builder()
				.user(user)
				.post(post)
				.build();
			return likeRepository.save(newLike);
		});
	}

	private Title createTitleIfNotFound(String name, String description, String color) {
		return titleRepository.findByName(name).orElseGet(() -> {
			Title newTitle = Title.builder()
				.name(name)
				.description(description)
				.color(color)
				.build();
			return titleRepository.save(newTitle);
		});
	}

	private UserTitle createUserTitleIfNotFound(Users user, Title title) {
		return userTitleRepository.findByUserAndTitle(user, title).orElseGet(() -> {
			UserTitle newUserTitle = UserTitle.builder()
				.user(user)
				.title(title)
				.earnedAt(LocalDateTime.now())
				.build();
			return userTitleRepository.save(newUserTitle);
		});
	}

	private UserChallenge createUserChallengeIfNotFound(Users user, Challenge challenge,
		Post post) {
		return userChallengeRepository.findByUserAndChallenge(user, challenge).orElseGet(() -> {
			UserChallenge newUserChallenge = UserChallenge.builder()
				.user(user)
				.challenge(challenge)
				.post(post)
				.completedAt(challenge.getChallengeDate())
				.build();
			return userChallengeRepository.save(newUserChallenge);
		});
	}

	private Character createCharacterIfNotFound(String name, int level, String imageUrl, Integer groupId) {
		return characterRepository.findByNameAndLevelAndGroupId(name, level, groupId).orElseGet(() -> {
			Character newCharacter = Character.builder()
				.name(name)
				.level(level)
				.imageUrl(imageUrl)
				.groupId(groupId)
				.build();
			return characterRepository.save(newCharacter);
		});
	}
}