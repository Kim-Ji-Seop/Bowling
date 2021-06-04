#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include <ctype.h>
#include <windows.h>

#define _CRT_SECURE_NO_WARNINGS
#pragma warning(disable : 4996)
typedef struct Member {
	int member_num;
	int game_round;
	int score;
}Bowling_crew;

Bowling_crew crew[125] = { NULL };
int z = 0;
double average(int a, int b, int c, int d, int e) {

}
int totalsum(int *frame,int frameNum) {
	int sum = 0;
	for (int i = 0; i < frameNum; i++) {
		printf("%d ", frame[i]);
		sum += frame[i];
	}
	printf("\n");
	return sum;
}
int Bowling_Score(int* arr, int data) {
	int f_score = 0;
	int frameScore;
	int j = 0;
	int frame[10] = { 0, };
	int is_strike[10] = { 0, };
	int is_spare[10] = { 0, };
	int score;
	for (int i = 0; i < data; i++) {
		if (j > 9) {
			break;
		}
		frameScore = 0;
		score = arr[i];
		frameScore += score;
		frame[j] += score;
		if (j - 2 >= 0 && is_strike[j - 2] > 0) {
			is_strike[j - 2] -= 1;
			frame[j - 2] += score;
		}
		if (j - 1 >= 0 && is_strike[j - 1] > 0) {
			is_strike[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (j - 1 >= 0 && is_spare[j - 1] > 0) {
			is_spare[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (score == 10) {
			is_strike[j] += 2;
			j++;//다음 프레임으로 이동
			continue;
		}
		i += 1;
		score = arr[i];
		frameScore += score;
		frame[j] += score;
		if (j - 1 >= 0 && is_strike[j - 1] > 0) {
			is_strike[j - 1] -= 1;
			frame[j - 1] += score;
		}
		if (frameScore == 10) {
			is_spare[j] += 1;
			j++;//다음 프레임으로 이동
			continue;
		}
		else {
			j++;
			continue;
		}

	}
	if (is_strike[9]) {
		score = arr[data - 2];
		if (is_strike[8]) {
			frame[8] += score;
			is_strike[8] -= 1;
		}
		frame[9] += score;
		score = arr[data-1];
		frame[9] += score;
	}
	else if (is_spare[9]) {
		score = arr[data - 1];
		frame[9] += score;
	}
	f_score = totalsum(frame, 10);

	return f_score;
	
}
void Split(char* arr)
{
	char* tk;
	int member[2];
	int frame_num[24] = { NULL };
	int i = 1;
	
	int member_score;
	tk = strtok(arr, " \t");
	
	member[0] = atoi(tk);
	crew[z].member_num = member[0];
	tk = strtok(NULL, " \t");

	member[1] = atoi(tk);
	crew[z].game_round = member[1];
	printf("%d %d\n\n", member[0],member[1]);
	tk = strtok(NULL, " \t");
	while (tk != NULL) {
		if (atoi(tk) != 10) {
			frame_num[i] = atoi(tk);
		}
		else {
			frame_num[i] = atoi(tk);
		}
		
		tk = strtok(NULL, " \t\n");
		i++;
	}
	printf("\n");
	int* str = malloc(sizeof(int) * i);
	printf("\n");
	for (int j = 0; j < i-1 ; j++) {
		str[j] = frame_num[j + 1];
		printf("%d ", str[j]);
	}
	
	printf("최종점수: %d ",  member_score = Bowling_Score(str, i - 1));
	crew[z].score = member_score;
	printf("\n");
	z++;
	free(str);
	
}

void test() {
	FILE* fp = fopen("bowling.txt", "r");
	char buf[100] = { NULL };
	int i = 0;
	int member,round;
	int frame[12] = { 0 };
	while (!feof(fp)) {
		if (fgets(buf, 100, fp) != NULL) {
			Split(buf);
		}
	}
	
	int j;
	int x;
	for (i = 0; i < 125; i++) {
		//printf("%d번 선수의 %d라운드 점수: %d\n", crew[i].member_num, crew[i].game_round, crew[i].score);
		x = 0;
		for (j = 0; j < i; j++) {
			if (crew[j].member_num == crew[i].member_num) {
				x += 1;
			}
		}
		if (x > 0) {
			continue;
		}
		for (j = i + 1; j < 125; j++) {
			if (crew[j].member_num == crew[i].member_num) {
				x += 1;
			}
		}
		if (x > 0) printf("%d %d\n", crew[i].member_num, crew[i].game_round);
		
		
	}
}
int main() {
	test();
}
